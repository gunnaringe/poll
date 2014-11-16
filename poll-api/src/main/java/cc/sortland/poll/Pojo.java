package cc.sortland.poll;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import java.util.UUID;
import javax.annotation.Nullable;

@AutoValue
public abstract class Pojo {
    @JsonCreator
    public static Pojo create(
            @Nullable @JsonProperty("id") final UUID id,
            @JsonProperty("name") final String name) {
        return new AutoValue_Pojo(id, name);
    }

    public abstract UUID getId();

    public abstract String getName();
}
