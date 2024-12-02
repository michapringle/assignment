package ca.mpringle.govt.service;

import ca.mpringle.govt.domain.InputData;
import ca.mpringle.govt.domain.OutputData;
import ca.mpringle.govt.util.MqttClientWrapper;
import ca.mpringle.govt.util.Topics;
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
public final class RulesEngineService {

    private static final String SERVER_URI = "tcp://test.mosquitto.org:1883";

    @NotNull
    private final MqttClientWrapper mqttClient;
    @NotNull
    private final WinterSupplementCalculatorService calculator;

    public RulesEngineService(
            @NotNull final MqttClientWrapper mqttClient,
            @NotNull final WinterSupplementCalculatorService calculator
            ) {

        this.mqttClient = Preconditions.checkNotNull(mqttClient);
        this.calculator = Preconditions.checkNotNull(calculator);
    }

    public static RulesEngineService create() throws MqttException {

        final MqttClientWrapper client = MqttClientWrapper.createConnected(
                SERVER_URI,
                "ca.mpringle.rulesengine"
        );

        return new RulesEngineService(
                client,
                new WinterSupplementCalculatorService()
        );
    }


    public void start() throws MqttException {

        // simulate government receive output data
        mqttClient.asyncReceive(
                Topics.getSubscribingInputTopic(),
                newAsyncMqttCallback(mqttClient, Topics.getPublishingOutputTopic())
        );
    }

    public void stop() throws MqttException {

        mqttClient.close();
    }

    MqttCallback newAsyncMqttCallback(
            @NotNull final MqttClientWrapper clientWrapper,
            @NotNull final String outputCalculationTopic) {

        return new MqttCallback() {
            @Override
            public void connectionLost(final Throwable cause) {
                log.info("Connection lost", cause);
            }

            @Override
            public void messageArrived(final String topic, final MqttMessage message) {

                log.info("Message arrived on topic {}", topic);
                try {
                    final InputData inputData = new ObjectMapper().readValue(message.getPayload(), InputData.class);

                    calculator.calculateWinterSupplement(inputData);
                    final OutputData outputData =  calculator.calculateWinterSupplement(inputData);

                    clientWrapper.publish(
                            outputCalculationTopic,
                            outputData.toJson().getBytes()
                    );

                } catch (final IOException logged) {
                    log.error("unable to parse message {}", new String(message.getPayload()), logged);
                } catch (final MqttException logged) {
                    log.error("unable to publish response message {}", new String(message.getPayload()), logged);
                }
            }

            @Override
            public void deliveryComplete(final IMqttDeliveryToken token) {
                log.info("Delivery complete");
            }
        };
    }
}
