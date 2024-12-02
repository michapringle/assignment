package ca.mpringle.govt.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.util.UUID;

@Immutable
@ThreadSafe
@EqualsAndHashCode
@Getter
@ToString
public final class OutputData {

    @NotNull
    // equal to InputData.id
    private final UUID id;
    // equal to InputData.familyUnitInPayForDecember
    private final boolean isEligible;
    private final float baseAmount;
    private final float childrenAmount;
    private final float supplementAmount;

    @Builder(toBuilder = true)
    @JsonCreator
    private OutputData(
            @JsonProperty("id") @NotNull final UUID id,
            @JsonProperty("isEligible") final boolean isEligible,
            @JsonProperty("baseAmount") final float baseAmount,
            @JsonProperty("childrenAmount") final float childrenAmount,
            @JsonProperty("supplementAmount") final float supplementAmount
    ) {

        this.id = Preconditions.checkNotNull(id, "The id cannot be null");
        this.isEligible = isEligible;
        this.baseAmount = baseAmount;
        this.childrenAmount = childrenAmount;
        this.supplementAmount = supplementAmount;

        Preconditions.checkArgument(
                this.baseAmount >= 0,
                "The baseAmount must be >= 0"
        );

        Preconditions.checkArgument(
                this.childrenAmount >= 0,
                "The childrenAmount must be >= 0"
        );

        Preconditions.checkArgument(
                this.supplementAmount >= 0,
                "The supplementAmount must be >= 0"
        );
    }

    @JsonProperty("isEligible")
    public boolean isEligible() {
        return isEligible;
    }

    @NotNull
    public String toJson() throws JsonProcessingException {

        return new ObjectMapper().writeValueAsString(this);
    }
}
