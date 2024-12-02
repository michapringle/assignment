package ca.mpringle.govt.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class InputDataTest {

    @Test
    void constructorShouldEnforceInvariants() {

        final InputData.InputDataBuilder builder = InputData
                .builder()
                .id(UUID.randomUUID())
                .numberOfChildren(0)
                .familyComposition(InputData.FamilyComposition.SINGLE)
                .familyUnitInPayForDecember(false);

        final InputData subjectUnderTest = assertDoesNotThrow(builder::build);

        assertThrows(
                NullPointerException.class,
                () -> subjectUnderTest.toBuilder().id(null).build()
        );

        assertThrows(
                NullPointerException.class,
                () -> subjectUnderTest.toBuilder().familyComposition(null).build()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> subjectUnderTest.toBuilder().numberOfChildren(-1).build()
        );
    }

    @Test
    void equalsAndHashcodeShouldObeyContract() {

        EqualsVerifier
                .forClass(InputData.class)
                .verify();
    }

    @Test
    void toStringShouldHaveCustomImplementation() {

        final InputData subjectUnderTest = InputData
                .builder()
                .id(UUID.randomUUID())
                .numberOfChildren(0)
                .familyComposition(InputData.FamilyComposition.SINGLE)
                .familyUnitInPayForDecember(false)
                .build();

        final String unexpected = subjectUnderTest
                .getClass().getName() + "@" + Integer.toHexString(subjectUnderTest.hashCode());

        assertNotEquals(unexpected, subjectUnderTest.toString());
    }
}
