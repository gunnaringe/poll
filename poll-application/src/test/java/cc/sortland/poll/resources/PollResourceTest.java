package cc.sortland.poll.resources;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import cc.sortland.poll.Id;
import cc.sortland.poll.Poll;
import cc.sortland.poll.PollManager;
import cc.sortland.poll.Vote;
import com.google.common.collect.ImmutableList;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PollResourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollResourceTest.class);

    private PollResource pollResource;

    @Mock
    private UriInfo uriInfo;

    @Before
    public void setup() {
        initMocks(this);
        final UriBuilder uriBuilder = UriBuilder.fromUri("http://example.com");
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);

        final PollManager pollManager = new PollManager();
        pollResource = new PollResource(pollManager);
    }

    @Test
    public void scenario() {
        final Poll poll = Poll.create("Pizza?", ImmutableList.of("Yes", "No"));
        final Response response = pollResource.postPoll(uriInfo, poll);
        assertThat(response.getStatus()).isEqualTo(201);
        final Poll retrievedPoll = (Poll) response.getEntity();
        LOGGER.debug("Response create poll: " + retrievedPoll);
        final Id id = retrievedPoll.id();
        assertThat(retrievedPoll.votes()).hasSize(0);

        for (int i = 0; i < 10; i++) {
            final Vote vote = Vote.create("Yes");
            final Response voteResponse = pollResource.postVote(uriInfo, id, vote);
            assertThat(voteResponse.getStatus()).isEqualTo(201);
        }

        for (int i = 0; i < 12; i++) {
            final Vote vote = Vote.create("No");
            final Response voteResponse = pollResource.postVote(uriInfo, id, vote);
            assertThat(voteResponse.getStatus()).isEqualTo(201);
        }

        final Response getPollResponse = pollResource.getPoll(uriInfo, id);
        assertThat(getPollResponse.getStatus()).isEqualTo(200);
        final Poll updatedPoll = (Poll) getPollResponse.getEntity();
        assertThat(updatedPoll.votes()).hasSize(22);
    }
}