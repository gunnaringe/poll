package cc.sortland.poll;

import cc.sortland.poll.resources.PollResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class PollApplication extends Application<PollConfiguration> {
    public static void main(final String[] args) throws Exception {
        new PollApplication().run(args);
    }

    @Override
    public String getName() {
        return "poll";
    }

    @Override
    public void initialize(Bootstrap<PollConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(final PollConfiguration configuration, final Environment environment) {
        {
            final PollManager pollManager = new PollManager();
            final PollResource pollResource = new PollResource(pollManager);
            environment.jersey().register(pollResource);
        }

        final ShallowHealthCheck healthCheck = new ShallowHealthCheck();
        environment.healthChecks().register("shallow", healthCheck);
    }
}