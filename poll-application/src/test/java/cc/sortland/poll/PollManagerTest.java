package cc.sortland.poll;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PollManagerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollManagerTest.class);


    private PollManager pollManager;

    @Before
    public void setup() {
        pollManager = new PollManager();
    }

    @Test
    public void summaryIsSortedAndCorrect() {
        Poll poll = createTestPoll();

        List<ReplySummary> votes = poll.getSortedSummary();
        assertThat(votes).isSortedAccordingTo((a, b) -> b.count() - a.count());
        assertThat(votes).hasSize(4);
        assertThat(votes.get(0)).isEqualTo(ReplySummary.create("Maybe", 18));
        assertThat(votes.get(1)).isEqualTo(ReplySummary.create("No", 15));
        assertThat(votes.get(2)).isEqualTo(ReplySummary.create("Yes", 13));
        assertThat(votes.get(3)).isEqualTo(ReplySummary.create("Something else", 1));
    }

    private Poll createTestPoll() {
        final Poll poll = pollManager.addPoll(
                Poll.create("Pizza?", ImmutableList.of("Yes", "No", "Maybe")));
        for (int i = 0; i < 13; i++) {
            pollManager.addVote(poll.id(), Vote.create("Yes"));
        }
        for (int i = 0; i < 15; i++) {
            pollManager.addVote(poll.id(), Vote.create("No"));
        }
        for (int i = 0; i < 18; i++) {
            pollManager.addVote(poll.id(), Vote.create("Maybe"));
        }
        pollManager.addVote(poll.id(), Vote.create("Something else"));
        return poll;
    }
}