package ca.mpringle.govt.util;

import com.google.common.base.Preconditions;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.NotThreadSafe;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@NotThreadSafe
@Slf4j
public class MqttClientWrapper {

    @NotNull
    private final IMqttClient mqttClient;

    MqttClientWrapper(@NotNull final IMqttClient mqttClient) {

        this.mqttClient = Preconditions.checkNotNull(mqttClient);
    }

    @NotNull
    static MqttConnectOptions createDefaultOptions() {

        final MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        return options;
    }

    @NotNull
    public static MqttClientWrapper createConnected(
            @NotNull final String serverUri,
            @NotNull final String clientId
    ) throws MqttException {

        log.info("creating MqttClientWrapper");
        final MqttConnectOptions options = createDefaultOptions();
        final IMqttClient mqttClient = new MqttClient(serverUri, clientId);
        mqttClient.connect(options);
        return new MqttClientWrapper(mqttClient);
    }

    public void publish(@NotNull final String topic, @NotNull final byte[] message) throws MqttException {

        final MqttMessage mqttMessage = new MqttMessage(message);
        mqttMessage.setQos(2);
        mqttClient.publish(topic, mqttMessage);
        log.info("Message published to topic {}", topic);
    }

    public void asyncReceive(
            @NotNull final String topic,
            @NotNull final MqttCallback mqttCallback
    ) throws MqttException {

        mqttClient.setCallback(mqttCallback);
        mqttClient.subscribe(topic);
    }

    public void close() throws MqttException {

        mqttClient.disconnect();
        log.info("disconnected from mqtt broker");
    }
}
