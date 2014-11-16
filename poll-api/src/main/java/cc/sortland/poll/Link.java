package cc.sortland.poll;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import java.net.URI;

@AutoValue
public abstract class Link {

    @JsonCreator
    public static Link create(
            @JsonProperty("rel") final String rel,
            @JsonProperty("method") final String method,
            @JsonProperty("href") final URI href) {
        return new AutoValue_Link(rel, method, href);
    }

    @JsonProperty("rel")
    public abstract String rel();

    @JsonProperty("method")
    public abstract String method();

    @JsonProperty("href")
    public abstract URI href();
}