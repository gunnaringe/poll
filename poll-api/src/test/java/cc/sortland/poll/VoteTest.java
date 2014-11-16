package cc.sortland.poll;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoteTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteTest.class);

    @Test
    public void should_set_id() {
        Vote vote = Vote.create("yes");
        LOGGER.info("Created vote: {}", vote);
    }
}