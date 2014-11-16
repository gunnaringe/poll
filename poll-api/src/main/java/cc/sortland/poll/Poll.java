package cc.sortland.poll;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

@AutoValue
public abstract class Poll {

    @JsonCreator
    public static Poll create(
            @JsonProperty("name") final String name,
            @JsonProperty("options") final List<String> options) {
        final Id id = Id.generate();
        final ConcurrentHashMap<Id, Vote> votes = new ConcurrentHashMap<>();
        return new AutoValue_Poll(id, ImmutableList.of(), name, options, votes);
    }

    Poll() {} // Avoid initialization.

    @JsonProperty("id")
    public abstract Id id();

    @JsonProperty("links")
    public abstract List<Link> links();

    @JsonProperty("name")
    public abstract String name();

    @Nullable
    @JsonProperty("options")
    public abstract List<String> options();

    @JsonIgnore
    public abstract ConcurrentHashMap<Id, Vote> votes();

    @JsonProperty("votes")
    public Map<String, List<Vote>> getVoteMap() {
        return votes().values().stream().collect(Collectors.groupingBy(Vote::reply));
    }

    @JsonProperty("summary")
    public List<ReplySummary> getSortedSummary() {
        return votes()
                .values()
                .parallelStream()
                .collect(Collectors.groupingBy(Vote::reply))
                .entrySet()
                .parallelStream()
                .map(entry -> ReplySummary.create(entry.getKey(), entry.getValue().size()))
                .sorted((a, b) -> b.count() - a.count())
                .collect(Collectors.toList());
    }

    public static Builder createBuilder(final Poll poll) {
        return new Builder(poll);
    }

    public static class Builder {
        private List<Link> links;
        private Poll poll;

        public Builder(final Poll poll) {
            links = poll.links();
            this.poll = poll;
        }

        public Builder links(final List<Link> links) {
            this.links = links;
            return this;
        }

        public Poll build() {
            return new AutoValue_Poll(poll.id(), links, poll.name(), poll.options(), poll.votes());
        }
    }
}
