package cc.sortland.poll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author gunnaringe
 */
public class PollManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollManager.class);

    private final Jedis jedis;
    private final ObjectMapper mapper;

    public PollManager() {
        jedis = new Jedis("localhost");
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Poll addPoll(final Poll poll) {
        LOGGER.info("Added poll: {}", poll);
        final String json;
        try {
             json = mapper.writeValueAsString(poll);
        } catch (final JsonProcessingException e) {
            LOGGER.warn("Could not serialize poll: {}", poll);
            throw new RuntimeException(e);
        }
        final String result = jedis.set(poll.id().getStringValue(), json);
        LOGGER.info("JEDIS result: {}", result);
        LOGGER.info("Saved value: {}", jedis.get(poll.id().getStringValue()));
        return poll;
    }

    public Optional<Poll> getPoll(final Id id) {
        LOGGER.info("Get poll with id={}", id);
        final String json = jedis.get(id.getStringValue());
        LOGGER.info("Received json: {}", json);
        final Poll poll;
        try {
            poll = mapper.readValue(json, Poll.class);
        } catch (final IOException e) {
            LOGGER.warn("Could not read value: {}", e.getMessage(), e);
            return Optional.empty();
        }
        return Optional.ofNullable(poll);
    }

    public boolean deletePoll(final Id id) {
        final Long status = jedis.del(id.getStringValue());
        LOGGER.debug("Delete with status: {}", status);
        return true;
    }

    public Optional<Vote> addVote(final Id pollId, final Vote vote) {
        final Optional<Poll> pollOptional = getPoll(pollId);
        if (!pollOptional.isPresent()) {
            return Optional.empty();
        }
        final Poll poll = pollOptional.get();
        LOGGER.info("Add vote to poll: {}", poll);
        poll.votes().put(vote.id(), vote);

        final String json;
        try {
            json = mapper.writeValueAsString(poll);
        } catch (final JsonProcessingException e) {
            LOGGER.warn("Could not serialize poll: {}", poll);
            throw new RuntimeException(e);
        }
        jedis.set(pollId.getStringValue(), json);
        return Optional.ofNullable(vote);
    }

    public Optional<Vote> getVote(Id pollId, final Id voteId) {
        final Optional<Poll> poll = getPoll(pollId);
        if (!poll.isPresent()) { return Optional.empty(); }
        return Optional.ofNullable(poll.get().votes().get(voteId));
    }

    public Optional<Vote> deleteVote(final Id pollId, final Id voteId) {
        final Optional<Poll> pollOptional = getPoll(pollId);
        if (!pollOptional.isPresent()) { return Optional.empty(); }
        final Poll poll = pollOptional.get();
        final Vote deleted = poll.votes().remove(voteId);
        addPoll(poll);
        return Optional.ofNullable(deleted);
    }
}
