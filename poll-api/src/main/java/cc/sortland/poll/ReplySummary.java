package cc.sortland.poll;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ReplySummary {
    @JsonCreator
    public static ReplySummary create(
            @JsonProperty("reply") final String name,
            @JsonProperty("count") final int count) {
        return new AutoValue_ReplySummary(name, count);
    }

    ReplySummary() {} // Avoid initialization.

    @JsonProperty("name")
    public abstract String name();

    @JsonProperty("count")
    public abstract int count();
}
