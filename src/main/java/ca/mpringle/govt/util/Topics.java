package ca.mpringle.govt.util;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.util.UUID;

@Immutable
@ThreadSafe
public final class Topics {

    private static final UUID ID = UUID.fromString("7eaa3905-1529-4af5-81d5-efd5db542f82");
    private static final String INPUT_DATA_TOPIC = "BRE/calculateWinterSupplementInput/";
    private static final String OUTPUT_CALCULATION_TOPIC = "BRE/calculateWinterSupplementOutput/";

    private Topics() {
    }

    public static UUID getTopicUuid() {

        return ID;
    }

    public static String getPublishingInputTopic() {

        return INPUT_DATA_TOPIC + ID;
    }

    public static String getSubscribingInputTopic() {

        return INPUT_DATA_TOPIC + ID;
    }

    public static String getPublishingOutputTopic() {

        return OUTPUT_CALCULATION_TOPIC + ID;
    }

    public static String getSubscribingOutputTopic() {

        return OUTPUT_CALCULATION_TOPIC + ID;
    }
}
