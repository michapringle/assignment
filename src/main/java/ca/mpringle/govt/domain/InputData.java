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
public final class InputData {

    public enum FamilyComposition {
        SINGLE,
        COUPLE
    }

    @NotNull
    private final UUID id;
    private final int numberOfChildren;
    @NotNull
    private final FamilyComposition familyComposition;
    private final boolean familyUnitInPayForDecember;

    @Builder(toBuilder = true)
    public InputData(
            @NotNull final UUID id,
            final int numberOfChildren,
            @NotNull final FamilyComposition familyComposition,
            final boolean familyUnitInPayForDecember
    ) {

        this.id = Preconditions.checkNotNull(id, "The id cannot be null");
        this.numberOfChildren = numberOfChildren;
        this.familyComposition = Preconditions.checkNotNull(familyComposition, "The familyComposition cannot be null");
        this.familyUnitInPayForDecember = familyUnitInPayForDecember;

        Preconditions.checkArgument(
                this.numberOfChildren >= 0,
                "The number of children must be >= 0"
        );
    }

    @JsonCreator
    private InputData(
            @JsonProperty("id") @NotNull final UUID id,
            @JsonProperty("numberOfChildren") final int numberOfChildren,
            @JsonProperty("familyComposition") @NotNull final String familyComposition,
            @JsonProperty("familyUnitInPayForDecember") final boolean familyUnitInPayForDecember
    ) {

        this(
                id,
                numberOfChildren,
                FamilyComposition.valueOf(familyComposition.toUpperCase()),
                familyUnitInPayForDecember
        );
    }

    @NotNull
    public String toJson() throws JsonProcessingException {

        return new ObjectMapper().writeValueAsString(this);
    }
}
