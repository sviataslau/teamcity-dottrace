package jetbrains.buildServer.dotTrace.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ValueAggregatorFactory {
    @Nullable
    ValueAggregator tryCreate(@NotNull final ThresholdValueType type);
}
