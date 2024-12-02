package ca.mpringle.govt.service;

import ca.mpringle.govt.domain.OutputData;
import ca.mpringle.govt.util.MqttClientWrapper;
import ca.mpringle.govt.util.Topics;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

final class GovernmentSimulatorServiceTest {

    @Test
    void constructorShouldEnforceInvariants() {

        final MqttClientWrapper mqttClient = mock(MqttClientWrapper.class);

        assertDoesNotThrow(() -> new GovernmentSimulatorService(mqttClient));

        assertThrows(
                NullPointerException.class,
                () -> new GovernmentSimulatorService(null)
        );
    }

    @Test
    void startShouldDelegateToMqttClientWrapper() throws MqttException, JsonProcessingException {

        // setup
        final MqttClientWrapper mqttClient = mock(MqttClientWrapper.class);

        final GovernmentSimulatorService subjectUnderTest = new GovernmentSimulatorService(mqttClient);

        // exercise
        subjectUnderTest.start();

        // verify
        verify(mqttClient).asyncReceive(any(String.class), any(MqttCallback.class));
        verify(mqttClient).publish(any(String.class), any(byte[].class));
    }

    @Test
    void stopShouldDelegateToMqttClientWrapper() throws MqttException {

        // setup
        final MqttClientWrapper mqttClient = mock(MqttClientWrapper.class);

        final GovernmentSimulatorService subjectUnderTest = new GovernmentSimulatorService(mqttClient);

        // exercise
        subjectUnderTest.stop();

        // verify
        verify(mqttClient).close();
    }

    @Test
    void newAsyncMqttCallbackShouldHandleIncomingMessage() throws Exception {

        final UUID id = UUID.randomUUID();

        final OutputData outputData = OutputData
                .builder()
                .id(id)
                .isEligible(false)
                .baseAmount(0F)
                .childrenAmount(0F)
                .supplementAmount(0F)
                .build();

        final MqttClientWrapper mqttClient = mock(MqttClientWrapper.class);
        final MqttMessage message = mock(MqttMessage.class);
        when(message.getPayload()).thenReturn(outputData.toJson().getBytes());
        final GovernmentSimulatorService service = new GovernmentSimulatorService(mqttClient);

        final MqttCallback subjectUnderTest = service.newAsyncMqttCallback();

        // exercise and verify
        assertDoesNotThrow(() -> subjectUnderTest.messageArrived(Topics.getPublishingOutputTopic(), message));
    }

    @Test
    void newAsyncMqttCallbackShouldHandleIOException() {


        final MqttClientWrapper mqttClient = mock(MqttClientWrapper.class);
        final MqttMessage message = mock(MqttMessage.class);
        when(message.getPayload()).thenReturn(new byte[]{92});
        final GovernmentSimulatorService service = new GovernmentSimulatorService(mqttClient);

        final MqttCallback subjectUnderTest = service.newAsyncMqttCallback();

        // exercise & verify
        assertDoesNotThrow(() -> subjectUnderTest.messageArrived(Topics.getPublishingOutputTopic(), message));
    }
}