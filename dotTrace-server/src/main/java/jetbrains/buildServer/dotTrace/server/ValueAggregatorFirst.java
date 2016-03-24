package jetbrains.buildServer.dotTrace.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public class ValueAggregatorFirst implements ValueAggregator {
    private boolean myIsCompleted = false;
    private BigDecimal myVal = null;

    @Override
    public void aggregate(@NotNull final BigDecimal value) {
        if (myIsCompleted) {
            return;
        }

        myIsCompleted = true;
        myVal = value;
    }

    @Override
    public boolean isCompleted() {
        return myIsCompleted;
    }

    @Nullable
    @Override
    public BigDecimal tryGetAggregatedValue() {
        return myVal;
    }
}
