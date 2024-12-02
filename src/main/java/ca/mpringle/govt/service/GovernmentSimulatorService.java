package ca.mpringle.govt.service;

import ca.mpringle.govt.domain.InputData;
import ca.mpringle.govt.domain.OutputData;
import ca.mpringle.govt.util.MqttClientWrapper;
import ca.mpringle.govt.util.Topics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.NotThreadSafe;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;

@NotThreadSafe
@Slf4j
public final class GovernmentSimulatorService {

    private static final String SERVER_URI = "tcp://test.mosquitto.org:1883";

    @NotNull
    private final MqttClientWrapper mqttClient;

    public GovernmentSimulatorService(@NotNull final MqttClientWrapper mqttClient) {

        this.mqttClient = Preconditions.checkNotNull(mqttClient);
    }

    public static GovernmentSimulatorService create() throws MqttException {

        final MqttClientWrapper client = MqttClientWrapper.createConnected(
                SERVER_URI,
                "ca.mpringle.government"
        );

        return new GovernmentSimulatorService(client);
    }

    public void start() throws MqttException, JsonProcessingException {

        // simulate government receive output data
        mqttClient.asyncReceive(
                Topics.getSubscribingOutputTopic(),
                newAsyncMqttCallback()
        );

        // simulate government publish input data
        final InputData inputData = InputData
                .builder()
                .id(Topics.getTopicUuid())
                .numberOfChildren(2)
                .familyComposition(InputData.FamilyComposition.COUPLE)
                .familyUnitInPayForDecember(true)
                .build();

        mqttClient.publish(
                Topics.getPublishingInputTopic(),
                inputData.toJson().getBytes()
        );
    }

    public void stop() throws MqttException {

        mqttClient.close();
    }

    @NotNull
    MqttCallback newAsyncMqttCallback() {

        return new MqttCallback() {
            @Override
            public void connectionLost(final Throwable cause) {
                log.info("Connection lost", cause);
            }

            @Override
            public void messageArrived(final String topic, final MqttMessage message) {

                log.info("Message arrived on topic {}", topic);
                try {
                    final OutputData outputData = new ObjectMapper().readValue(message.getPayload(), OutputData.class);
                    log.info("Received message {} on topic {}", outputData, topic);

                } catch (final IOException logged) {
                    log.error("unable to parse message {}", new String(message.getPayload()), logged);
                }
            }

            @Override
            public void deliveryComplete(final IMqttDeliveryToken token) {
                log.info("Delivery complete");
            }
        };
    }
}
