package cc.sortland.poll;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PollTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollTest.class);

    @Test
    public void empty_id_generates_a_random_one() {
        List<String> options = ImmutableList.of();
        final Poll poll = Poll.create("name", options);
        LOGGER.info("Created poll: {}", poll);
        assertThat(poll.id()).isNotNull();
    }
}
