package cc.sortland.poll;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@AutoValue
public abstract class Poll {

    public static Poll create(final String name, final List<String> options) {
        return create(name, options, Maps.newConcurrentMap());
    }

    @JsonCreator
    public static Poll create(
            @JsonProperty("name") final String name,
            @JsonProperty("options") final List<String> options,
            @JsonProperty("votes") final Map<String, List<Vote>> votes) {
        final ConcurrentHashMap<Id, Vote> voteMap = new ConcurrentHashMap<>();
        for (List<Vote> vote : votes.values()) {
            for (Vote v : vote) {
                voteMap.put(v.id(), v);
            }
        }
        return new AutoValue_Poll(
                Id.generate(),
                ImmutableList.<Link>of(),
                name,
                MoreObjects.firstNonNull(options, ImmutableList.<String>of()),
                voteMap);
    }

    Poll() {} // Avoid initialization.

    @JsonProperty("id")
    public abstract Id id();

    private void setId(Id id) {}

    @JsonProperty("links")
    public abstract List<Link> links();

    private void setLinks(List<Link> links) {}

    @JsonProperty("name")
    public abstract String name();

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

    private void setSummary(List<ReplySummary> summaries) {}

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
