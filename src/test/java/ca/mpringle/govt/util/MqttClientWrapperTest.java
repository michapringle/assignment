package ca.mpringle.govt.util;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

final class MqttClientWrapperTest {

    @Test
    void constructorShouldEnforceInvariants() {

        final IMqttClient mqttClient = mock(IMqttClient.class);

        assertDoesNotThrow(() -> new MqttClientWrapper(mqttClient));

        assertThrows(
                NullPointerException.class,
                () -> new MqttClientWrapper(null)
        );
    }

    @Test
    void createDefaultOptionsShouldCreateValidOptions() throws MqttException {

        // test
        final MqttConnectOptions defaultOptions = assertDoesNotThrow(MqttClientWrapper::createDefaultOptions);

        // verify
        assertNotNull(defaultOptions);
    }

    @Test
    void publishShouldDelegateToIMqttClient() throws MqttException {

        // setup
        final IMqttClient mqttClient = mock(IMqttClient.class);
        final MqttClientWrapper subjectUnderTest = new MqttClientWrapper(mqttClient);

        // test
        subjectUnderTest.publish("topic", new byte[]{40});

        // verify
        verify(mqttClient).publish(any(String.class), any(MqttMessage.class));
    }

    @Test
    void asyncReceiveShouldDelegateToIMqttClient() throws MqttException {

        // setup
        final IMqttClient mqttClient = mock(IMqttClient.class);
        final MqttCallback callback = mock(MqttCallback.class);
        final MqttClientWrapper subjectUnderTest = new MqttClientWrapper(mqttClient);
        final String topic = "topic";

        // test
        subjectUnderTest.asyncReceive(topic, callback);

        // verify
        verify(mqttClient).setCallback(callback);
        verify(mqttClient).subscribe(topic);
    }

    @Test
    void closeShouldDelegateToIMqttClient() throws MqttException {

        // setup
        final IMqttClient mqttClient = mock(IMqttClient.class);
        final MqttClientWrapper subjectUnderTest = new MqttClientWrapper(mqttClient);

        // test
        subjectUnderTest.close();

        // verify
        verify(mqttClient).disconnect();
    }
}
