package ca.mpringle.govt.service;

import ca.mpringle.govt.domain.InputData;
import ca.mpringle.govt.domain.OutputData;
import ca.mpringle.govt.util.MqttClientWrapper;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

final class RulesEngineServiceTest {

    @Test
    void constructorShouldEnforceInvariants() {

        final MqttClientWrapper mqttClient = mock(MqttClientWrapper.class);
        final WinterSupplementCalculatorService calculator = mock(WinterSupplementCalculatorService.class);

        assertDoesNotThrow(() -> new RulesEngineService(mqttClient, calculator));

        assertThrows(
                NullPointerException.class,
                () -> new RulesEngineService(null, null)
        );

        assertThrows(
                NullPointerException.class,
                () -> new RulesEngineService(null, calculator)
        );

        assertThrows(
                NullPointerException.class,
                () -> new RulesEngineService(mqttClient, null)
        );
    }

    @Test
    void startShouldDelegateToMqttClientWrapper() throws MqttException {

        // setup
        final MqttClientWrapper mqttClient = mock(MqttClientWrapper.class);
        final WinterSupplementCalculatorService calculator = mock(WinterSupplementCalculatorService.class);

        final RulesEngineService subjectUnderTest = new RulesEngineService(mqttClient, calculator);

        // exercise
        subjectUnderTest.start();

        // verify
        verify(mqttClient).asyncReceive(any(String.class), any(MqttCallback.class));
    }

    @Test
    void stopShouldDelegateToMqttClientWrapper() throws MqttException {

        // setup
        final MqttClientWrapper mqttClient = mock(MqttClientWrapper.class);
        final WinterSupplementCalculatorService calculator = mock(WinterSupplementCalculatorService.class);

        final RulesEngineService subjectUnderTest = new RulesEngineService(mqttClient, calculator);

        // exercise
        subjectUnderTest.stop();

        // verify
        verify(mqttClient).close();
    }

    @Test
    void newAsyncMqttCallbackShouldHandleIncomingMessage() throws Exception {

        final UUID id = UUID.randomUUID();
        // setup
        final InputData inputData = InputData
                .builder()
                .id(id)
                .numberOfChildren(0)
                .familyComposition(InputData.FamilyComposition.SINGLE)
                .familyUnitInPayForDecember(false)
                .build();

        final OutputData outputData = OutputData
                .builder()
                .id(id)
                .isEligible(false)
                .baseAmount(0F)
                .childrenAmount(0F)
                .supplementAmount(0F)
                .build();

        final MqttClientWrapper mqttClient = mock(MqttClientWrapper.class);
        final WinterSupplementCalculatorService calculator = mock(WinterSupplementCalculatorService.class);
        final MqttMessage message = mock(MqttMessage.class);
        when(message.getPayload()).thenReturn(inputData.toJson().getBytes());
        when(calculator.calculateWinterSupplement(inputData)).thenReturn(outputData);
        final RulesEngineService service = new RulesEngineService(mqttClient, calculator);

        final String topic = "topic";
        final MqttCallback subjectUnderTest = service.newAsyncMqttCallback(mqttClient, topic);

        // exercise
        subjectUnderTest.messageArrived(topic, message);

        // verify
        verify(mqttClient).publish(any(String.class), any(byte[].class));
    }

    @Test
    void newAsyncMqttCallbackShouldHandleIOException() throws Exception {

        final MqttClientWrapper mqttClient = mock(MqttClientWrapper.class);
        final WinterSupplementCalculatorService calculator = mock(WinterSupplementCalculatorService.class);
        final MqttMessage message = mock(MqttMessage.class);
        when(message.getPayload()).thenReturn(new byte[]{92});
        final RulesEngineService service = new RulesEngineService(mqttClient, calculator);

        final String topic = "topic";
        final MqttCallback subjectUnderTest = service.newAsyncMqttCallback(mqttClient, topic);

        // exercise
        assertDoesNotThrow(() -> subjectUnderTest.messageArrived(topic, message));

        // verify
        verify(mqttClient, times(0)).publish(any(String.class), any(byte[].class));
    }

    @Test
    void newAsyncMqttCallbackShouldHandleMqttException() throws Exception {

        final UUID id = UUID.randomUUID();
        // setup
        final InputData inputData = InputData
                .builder()
                .id(id)
                .numberOfChildren(0)
                .familyComposition(InputData.FamilyComposition.SINGLE)
                .familyUnitInPayForDecember(false)
                .build();

        final OutputData outputData = OutputData
                .builder()
                .id(id)
                .isEligible(false)
                .baseAmount(0F)
                .childrenAmount(0F)
                .supplementAmount(0F)
                .build();

        final MqttClientWrapper mqttClient = mock(MqttClientWrapper.class);
        final WinterSupplementCalculatorService calculator = mock(WinterSupplementCalculatorService.class);
        final MqttMessage message = mock(MqttMessage.class);
        final String topic = "topic";
        when(message.getPayload()).thenReturn(inputData.toJson().getBytes());
        when(calculator.calculateWinterSupplement(inputData)).thenReturn(outputData);
        doThrow(new MqttException(1)).when(mqttClient).publish(any(String.class), any(byte[].class));
        final RulesEngineService service = new RulesEngineService(mqttClient, calculator);

        final MqttCallback subjectUnderTest = service.newAsyncMqttCallback(mqttClient, topic);

        // exercise
        assertDoesNotThrow(() -> subjectUnderTest.messageArrived(topic, message));

        // verify
        verify(mqttClient).publish(any(String.class), any(byte[].class));
    }
}