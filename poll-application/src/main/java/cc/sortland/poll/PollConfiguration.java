package cc.sortland.poll;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;
import org.hibernate.validator.constraints.NotEmpty;

public class PollConfiguration extends Configuration {
    @Nullable //NotEmpty
    private String name;

    @JsonProperty
    public String getName() {
        return name;
    }

}