package ca.mpringle.govt.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class OutputDataTest {

    @Test
    void constructorShouldEnforceInvariants() {

        final OutputData.OutputDataBuilder builder = OutputData
                .builder()
                .id(UUID.randomUUID())
                .isEligible(true)
                .baseAmount(0F)
                .childrenAmount(0F)
                .supplementAmount(0F);

        final OutputData subjectUnderTest = assertDoesNotThrow(builder::build);

        assertThrows(
                NullPointerException.class,
                () -> subjectUnderTest.toBuilder().id(null).build()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> subjectUnderTest.toBuilder().baseAmount(-1F).build()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> subjectUnderTest.toBuilder().childrenAmount(-1F).build()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> subjectUnderTest.toBuilder().supplementAmount(-1F).build()
        );
    }

    @Test
    void equalsAndHashcodeShouldObeyContract() {

        EqualsVerifier
                .forClass(OutputData.class)
                .verify();
    }

    @Test
    void toStringShouldHaveCustomImplementation() {

        final OutputData subjectUnderTest = OutputData
                .builder()
                .id(UUID.randomUUID())
                .isEligible(true)
                .baseAmount(0F)
                .childrenAmount(0F)
                .supplementAmount(0F)
                .build();

        final String unexpected = subjectUnderTest
                .getClass().getName() + "@" + Integer.toHexString(subjectUnderTest.hashCode());

        assertNotEquals(unexpected, subjectUnderTest.toString());
    }
}
