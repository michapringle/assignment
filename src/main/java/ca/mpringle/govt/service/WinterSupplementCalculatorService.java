package ca.mpringle.govt.service;

import ca.mpringle.govt.domain.InputData;
import ca.mpringle.govt.domain.OutputData;
import jakarta.validation.constraints.NotNull;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

@Immutable
@ThreadSafe
public class WinterSupplementCalculatorService {

    @NotNull
    public OutputData calculateWinterSupplement(@NotNull final InputData inputData) {

        final OutputData.OutputDataBuilder builder = OutputData
                .builder()
                .id(inputData.getId())
                .isEligible(inputData.isFamilyUnitInPayForDecember());

        if (!inputData.isFamilyUnitInPayForDecember()) {

            return builder
                    .baseAmount(0F)
                    .childrenAmount(0F)
                    .supplementAmount(0F)
                    .build();
        }

        if (inputData.getNumberOfChildren() == 0) {
            final boolean isSingle = inputData.getFamilyComposition().equals(InputData.FamilyComposition.SINGLE);
            final float amount = isSingle ? 60F : 120F;
            return builder
                    .baseAmount(amount)
                    .childrenAmount(0F)
                    .supplementAmount(amount)
                    .build();
        }

        return builder
                .baseAmount(120F)
                .childrenAmount(inputData.getNumberOfChildren() * 20F)
                .supplementAmount(120F + inputData.getNumberOfChildren() * 20F)
                .build();
    }
}
