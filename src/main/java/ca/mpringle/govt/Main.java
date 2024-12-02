package ca.mpringle.govt;

import ca.mpringle.govt.service.GovernmentSimulatorService;
import ca.mpringle.govt.service.RulesEngineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {

    public static void main(final String[] args) {

        final long runTimeInSeconds = 20L;
        newRulesEngineThread(runTimeInSeconds).start();
        newGovernmentSimulatorThread(runTimeInSeconds).start();
    }

    @NotNull
    private static Thread newRulesEngineThread(final long runTimeInSeconds) {

        return new Thread(() -> {

            try {
                runRulesEngine(runTimeInSeconds);
            } catch (MqttException logged) {
                log.error("Error running rules engine", logged);
            } catch (InterruptedException handled) {
                Thread.currentThread().interrupt();
                log.error("Error running rules engine", handled);
            }
        });
    }

    private static void runRulesEngine(final long runTimeInSeconds) throws
            MqttException,
            InterruptedException {

        final RulesEngineService rulesEngineService = RulesEngineService.create();

        try {
            rulesEngineService.start();
            TimeUnit.SECONDS.sleep(runTimeInSeconds);

        } finally {
            rulesEngineService.stop();
        }
    }

    @NotNull
    private static Thread newGovernmentSimulatorThread(final long runTimeInSeconds) {

        return new Thread(() -> {

            try {
                runGovernmentSimulatorOnce(runTimeInSeconds);
            } catch (MqttException | JsonProcessingException logged) {
                log.error("Error running rules engine", logged);
            } catch (InterruptedException handled) {
                Thread.currentThread().interrupt();
                log.error("Error running rules engine", handled);
            }
        });
    }

    private static void runGovernmentSimulatorOnce(final long runTimeInSeconds) throws
            MqttException,
            JsonProcessingException,
            InterruptedException {

        final GovernmentSimulatorService governmentSimulator = GovernmentSimulatorService.create();

        try {
            governmentSimulator.start();
            TimeUnit.SECONDS.sleep(runTimeInSeconds);

        } finally {
            governmentSimulator.stop();
        }
    }
}
