package jetbrains.buildServer.dotTrace.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public interface ValueAggregator {
    void aggregate(@NotNull final BigDecimal value);

    boolean isCompleted();

    @Nullable
    BigDecimal tryGetAggregatedValue();
}
