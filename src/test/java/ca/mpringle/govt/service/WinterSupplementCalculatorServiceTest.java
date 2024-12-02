package ca.mpringle.govt.service;

import ca.mpringle.govt.domain.InputData;
import ca.mpringle.govt.domain.OutputData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class WinterSupplementCalculatorServiceTest {

    @ParameterizedTest
    @MethodSource("calculateWinterSupplementData")
    void calculateWinterSupplementShouldCalculateCorrectSupplement(final InputData inputData, final OutputData expected) {

        final OutputData actual = new WinterSupplementCalculatorService().calculateWinterSupplement(inputData);

        assertEquals(expected, actual);
    }

    static Stream<Arguments> calculateWinterSupplementData() {

        final UUID id = UUID.randomUUID();

        final OutputData notAllowed = OutputData
                .builder()
                .id(id)
                .isEligible(false)
                .baseAmount(0F)
                .childrenAmount(0F)
                .supplementAmount(0F)
                .build();

        return Stream.of(

                // winter supplement not allowed - single parent with no dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(0)
                                .familyComposition(InputData.FamilyComposition.SINGLE)
                                .familyUnitInPayForDecember(false)
                                .build(),
                        notAllowed
                ),

                // winter supplement not allowed - single parent with one dependent
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(1)
                                .familyComposition(InputData.FamilyComposition.SINGLE)
                                .familyUnitInPayForDecember(false)
                                .build(),
                        notAllowed
                ),

                // winter supplement not allowed - single parent with two dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(2)
                                .familyComposition(InputData.FamilyComposition.SINGLE)
                                .familyUnitInPayForDecember(false)
                                .build(),
                        notAllowed
                ),

                // winter supplement not allowed - single parent with three dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(3)
                                .familyComposition(InputData.FamilyComposition.SINGLE)
                                .familyUnitInPayForDecember(false)
                                .build(),
                        notAllowed
                ),

                // winter supplement not allowed - single parent with four dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(4)
                                .familyComposition(InputData.FamilyComposition.SINGLE)
                                .familyUnitInPayForDecember(false)
                                .build(),
                        notAllowed
                ),

                // winter supplement not allowed - couple with no dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(0)
                                .familyComposition(InputData.FamilyComposition.COUPLE)
                                .familyUnitInPayForDecember(false)
                                .build(),
                        notAllowed
                ),

                // winter supplement not allowed - couple with one dependent
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(1)
                                .familyComposition(InputData.FamilyComposition.COUPLE)
                                .familyUnitInPayForDecember(false)
                                .build(),
                        notAllowed
                ),

                // winter supplement not allowed - couple with two dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(2)
                                .familyComposition(InputData.FamilyComposition.COUPLE)
                                .familyUnitInPayForDecember(false)
                                .build(),
                        notAllowed
                ),

                // winter supplement not allowed - couple with three dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(3)
                                .familyComposition(InputData.FamilyComposition.COUPLE)
                                .familyUnitInPayForDecember(false)
                                .build(),
                        notAllowed
                ),

                // winter supplement not allowed - couple with four dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(4)
                                .familyComposition(InputData.FamilyComposition.COUPLE)
                                .familyUnitInPayForDecember(false)
                                .build(),
                        notAllowed
                ),


                // single parent with no dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(0)
                                .familyComposition(InputData.FamilyComposition.SINGLE)
                                .familyUnitInPayForDecember(true)
                                .build(),
                        OutputData
                                .builder()
                                .id(id)
                                .isEligible(true)
                                .baseAmount(60F)
                                .childrenAmount(0F)
                                .supplementAmount(60F)
                                .build()
                ),

                // single parent with one dependent
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(1)
                                .familyComposition(InputData.FamilyComposition.SINGLE)
                                .familyUnitInPayForDecember(true)
                                .build(),
                        OutputData
                                .builder()
                                .id(id)
                                .isEligible(true)
                                .baseAmount(120F)
                                .childrenAmount(20F)
                                .supplementAmount(140F)
                                .build()
                ),

                // single parent with two dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(2)
                                .familyComposition(InputData.FamilyComposition.SINGLE)
                                .familyUnitInPayForDecember(true)
                                .build(),
                        OutputData
                                .builder()
                                .id(id)
                                .isEligible(true)
                                .baseAmount(120F)
                                .childrenAmount(40F)
                                .supplementAmount(160F)
                                .build()
                ),

                // single parent with three dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(3)
                                .familyComposition(InputData.FamilyComposition.SINGLE)
                                .familyUnitInPayForDecember(true)
                                .build(),
                        OutputData
                                .builder()
                                .id(id)
                                .isEligible(true)
                                .baseAmount(120F)
                                .childrenAmount(60F)
                                .supplementAmount(180F)
                                .build()
                ),

                // single parent with four dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(4)
                                .familyComposition(InputData.FamilyComposition.SINGLE)
                                .familyUnitInPayForDecember(true)
                                .build(),
                        OutputData
                                .builder()
                                .id(id)
                                .isEligible(true)
                                .baseAmount(120F)
                                .childrenAmount(80F)
                                .supplementAmount(200F)
                                .build()
                ),

                // couple with no dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(0)
                                .familyComposition(InputData.FamilyComposition.COUPLE)
                                .familyUnitInPayForDecember(true)
                                .build(),
                        OutputData
                                .builder()
                                .id(id)
                                .isEligible(true)
                                .baseAmount(120F)
                                .childrenAmount(0F)
                                .supplementAmount(120F)
                                .build()
                ),

                // couple with one dependent
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(1)
                                .familyComposition(InputData.FamilyComposition.COUPLE)
                                .familyUnitInPayForDecember(true)
                                .build(),
                        OutputData
                                .builder()
                                .id(id)
                                .isEligible(true)
                                .baseAmount(120F)
                                .childrenAmount(20F)
                                .supplementAmount(140F)
                                .build()
                ),

                // couple with two dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(2)
                                .familyComposition(InputData.FamilyComposition.COUPLE)
                                .familyUnitInPayForDecember(true)
                                .build(),
                        OutputData
                                .builder()
                                .id(id)
                                .isEligible(true)
                                .baseAmount(120F)
                                .childrenAmount(40F)
                                .supplementAmount(160F)
                                .build()
                ),

                // couple with three dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(3)
                                .familyComposition(InputData.FamilyComposition.COUPLE)
                                .familyUnitInPayForDecember(true)
                                .build(),
                        OutputData
                                .builder()
                                .id(id)
                                .isEligible(true)
                                .baseAmount(120F)
                                .childrenAmount(60F)
                                .supplementAmount(180F)
                                .build()
                ),

                // couple with four dependents
                Arguments.of(
                        InputData
                                .builder()
                                .id(id)
                                .numberOfChildren(4)
                                .familyComposition(InputData.FamilyComposition.COUPLE)
                                .familyUnitInPayForDecember(true)
                                .build(),
                        OutputData
                                .builder()
                                .id(id)
                                .isEligible(true)
                                .baseAmount(120F)
                                .childrenAmount(80F)
                                .supplementAmount(200F)
                                .build()
                )
        );
    }
}
