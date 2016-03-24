package jetbrains.buildServer.dotTrace.server;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public interface MetricComparer {
    boolean isMeasuredValueWithinThresholds(@NotNull final BigDecimal prevValue, @NotNull final BigDecimal measuredValue, @NotNull final ThresholdValue threshold);

    BigDecimal tryGetThresholdValue(@NotNull final BigDecimal prevValue, @NotNull final ThresholdValue threshold);
}
