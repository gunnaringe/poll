package cc.sortland.poll;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Vote {
    @JsonCreator
    public static Vote create(@JsonProperty("reply") final String reply) {
        final Id id = Id.generate();
        return new AutoValue_Vote(id, reply);
    }

    Vote() {} // Avoid initialization.

    @JsonProperty("id")
    public abstract Id id();

    @JsonProperty("reply")
    public abstract String reply();
}
