package jetbrains.buildServer.dotTrace.server;

import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public class ValueAggregatorFactoryImpl implements ValueAggregatorFactory {
  private final ValueAggregator myValueAggregatorFirst;
  private final ValueAggregator myValueAggregatorLast;
  private final ValueAggregator myValueAggregatorAverage;

  public ValueAggregatorFactoryImpl(
    @NotNull final ValueAggregator valueAggregatorFirst,
    @NotNull final ValueAggregator valueAggregatorLast,
    @NotNull final ValueAggregator valueAggregatorAverage) {
    myValueAggregatorFirst = valueAggregatorFirst;
    myValueAggregatorLast = valueAggregatorLast;
    myValueAggregatorAverage = valueAggregatorAverage;
  }

  @Nullable
  @Override
  public ValueAggregator tryCreate(@NotNull final ThresholdValueType type) {
    switch (type) {
      case FIRST:
        return myValueAggregatorFirst;

      case LAST:
        return myValueAggregatorLast;

      case AVERAGE:
        return myValueAggregatorAverage;

      default:
        return null;
    }
  }
}
