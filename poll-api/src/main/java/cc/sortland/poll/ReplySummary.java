package cc.sortland.poll;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class ReplySummary {
    @JsonCreator
    public static ReplySummary create(
            @JsonProperty("reply") final String name,
            @JsonProperty("count") final int count) {
        return new AutoValue_ReplySummary(name, count);
    }

    ReplySummary() {} // Avoid initialization.

    @Nullable
    @JsonProperty("name")
    public abstract String name();

    @Nullable
    @JsonProperty("count")
    public abstract int count();
}
