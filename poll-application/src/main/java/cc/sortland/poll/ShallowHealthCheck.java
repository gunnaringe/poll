package cc.sortland.poll;

import com.codahale.metrics.health.HealthCheck;

public class ShallowHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}