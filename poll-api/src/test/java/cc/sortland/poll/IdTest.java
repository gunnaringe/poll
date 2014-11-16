package cc.sortland.poll;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdTest.class);

    @Test
    public void canConvertToAndFromString() {
        final String value = "WKQIK6WevwKv2IKfxi0g-aE8nEH4";
        final Id id = Id.valueOf(value);
        LOGGER.debug("Created id: {}", id);
        assertThat(id.getStringValue()).isEqualTo(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void willThrowExceptionIfWrongInput() {
        Id.valueOf("w-xOJzdPSCKy0otaRDXafQ");
    }

    @Test
    public void generateOk() {
        final Id id = Id.generate();
        LOGGER.debug("Created id: {}", id);

        assertThat(id.getStringValue()).hasSize(28);
    }
}