package jetbrains.buildServer.dotTrace.server;

import jetbrains.buildServer.dotTrace.StatisticMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StatisticProvider {
    @Nullable
    Statistic tryCreateStatistic(@NotNull final StatisticMessage statisticMessage, @NotNull final Iterable<HistoryElement> history);
}
