package jetbrains.buildServer.dotTrace.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public class ValueAggregatorLast implements ValueAggregator {
    private BigDecimal myVal = null;

    @Override
    public void aggregate(@NotNull final BigDecimal value) {
        myVal = value;
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Nullable
    @Override
    public BigDecimal tryGetAggregatedValue() {
        return myVal;
    }
}
