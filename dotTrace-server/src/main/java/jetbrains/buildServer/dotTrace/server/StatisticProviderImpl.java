package jetbrains.buildServer.dotTrace.server;

import jetbrains.buildServer.dotTrace.StatisticMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.BeanFactory;

import java.math.BigDecimal;

public class StatisticProviderImpl implements StatisticProvider {
    private final BigDecimalParser myBigDecimalParser;
    private final StatisticKeyFactory myStatisticKeyFactory;
    private final BeanFactory myBeanFactory;

    public StatisticProviderImpl(
            @NotNull final BigDecimalParser bigDecimalParser,
            @NotNull final StatisticKeyFactory statisticKeyFactory,
            @NotNull final BeanFactory beanFactory) {
        myBigDecimalParser = bigDecimalParser;
        myStatisticKeyFactory = statisticKeyFactory;
        myBeanFactory = beanFactory;
    }

    @Nullable
    @Override
    public Statistic tryCreateStatistic(@NotNull final StatisticMessage statisticMessage, @NotNull final Iterable<HistoryElement> historyProviders) {
        final String methodName = statisticMessage.getMethodName();
        @Nullable final BigDecimal measuredTotalTime = myBigDecimalParser.tryParseBigDecimal(statisticMessage.getMeasuredTotalTime());
        @Nullable final BigDecimal measuredOwnTime = myBigDecimalParser.tryParseBigDecimal(statisticMessage.getMeasuredOwnTime());
        @Nullable final ThresholdValue totalTimeThreshold = ThresholdValue.tryParse(statisticMessage.getTotalTimeThreshold());
        @Nullable final ThresholdValue ownTimeThreshold = ThresholdValue.tryParse(statisticMessage.getOwnTimeThreshold());
        if (measuredTotalTime == null || measuredOwnTime == null || totalTimeThreshold == null || ownTimeThreshold == null) {
            return null;
        }

        final String totalTimeKey = myStatisticKeyFactory.createTotalTimeKey(methodName);
        final String ownTimeKey = myStatisticKeyFactory.createOwnTimeKey(methodName);

        final ValueAggregatorFactory valueAggregatorFactory = myBeanFactory.getBean(ValueAggregatorFactory.class);
        final ValueAggregator totalTimeAgg = valueAggregatorFactory.tryCreate(totalTimeThreshold.getType());
        final ValueAggregator ownTimeAgg = valueAggregatorFactory.tryCreate(ownTimeThreshold.getType());

        for (HistoryElement historyElement : historyProviders) {
            @Nullable final BigDecimal totalTimeVal = historyElement.tryGetValue(totalTimeKey);
            if (totalTimeAgg != null && totalTimeVal != null && BigDecimal.ZERO.compareTo(totalTimeVal) < 0) {
                totalTimeAgg.aggregate(totalTimeVal);
            }

            @Nullable final BigDecimal ownTimeVal = historyElement.tryGetValue(ownTimeKey);
            if (ownTimeAgg != null && ownTimeVal != null && BigDecimal.ZERO.compareTo(ownTimeVal) < 0) {
                ownTimeAgg.aggregate(ownTimeVal);
            }

            if ((totalTimeAgg == null || totalTimeAgg.isCompleted()) && (ownTimeAgg == null || ownTimeAgg.isCompleted())) {
                break;
            }
        }

        @Nullable final BigDecimal prevTotalTime = GetPrevValue(totalTimeAgg, totalTimeThreshold);
        @Nullable final BigDecimal prevOwnTime = GetPrevValue(ownTimeAgg, ownTimeThreshold);
        return new Statistic(measuredTotalTime, measuredOwnTime, totalTimeThreshold, ownTimeThreshold, prevTotalTime, prevOwnTime);
    }


    @Nullable
    private BigDecimal GetPrevValue(@Nullable final ValueAggregator timeAgg, @NotNull final ThresholdValue timeThreshold) {
        return timeAgg != null
                ? timeAgg.tryGetAggregatedValue()
                : timeThreshold.getType() == ThresholdValueType.ABSOLUTE ? timeThreshold.getValue() : null;
    }
}