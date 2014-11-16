package cc.sortland.poll;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gunnaringe
 */
public class PollManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollManager.class);

    private final ConcurrentMap<Id, Poll> polls;

    public PollManager() {
        polls = Maps.newConcurrentMap();
    }

    public Poll addPoll(final Poll poll) {
        polls.put(poll.id(), poll);
        LOGGER.info("Added poll: {}", poll);
        return poll;
    }

    public Optional<Poll> getPoll(final Id id) {
        LOGGER.info("Get poll with id={}", id);
        return Optional.ofNullable(polls.get(id));
    }

    public Optional<Poll> deletePoll(final Id id) {
        return Optional.ofNullable(polls.remove(id));
    }

    public Optional<Vote> addVote(final Id pollId, final Vote vote) {
        final Optional<Poll> pollOptional = getPoll(pollId);
        if (!pollOptional.isPresent()) {
            return Optional.empty();
        }
        final Poll poll = pollOptional.get();
        poll.votes().put(vote.id(), vote);
        return Optional.ofNullable(vote);
    }

    public Optional<Vote> getVote(Id pollId, final Id voteId) {
        final Poll poll = polls.get(pollId);
        return Optional.ofNullable(poll.votes().get(voteId));
    }

    public Optional<Vote> deleteVote(final Id pollId, final Id voteId) {
        final Optional<Poll> pollOptional = getPoll(pollId);
        if (!pollOptional.isPresent()) { return Optional.empty(); }
        final Poll poll = pollOptional.get();
        return Optional.ofNullable(poll.votes().remove(voteId));
    }
}
