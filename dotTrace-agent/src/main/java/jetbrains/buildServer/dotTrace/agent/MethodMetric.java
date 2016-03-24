package jetbrains.buildServer.dotTrace.agent;

import org.jetbrains.annotations.NotNull;

public class MethodMetric extends MetricBase {
    private final String myMethodName;

    public MethodMetric(
            @NotNull final String methodName,
            @NotNull final String totalTime,
            @NotNull final String ownTime) {
        super(totalTime, ownTime);
        myMethodName = methodName;
    }

    @NotNull
    public String getMethodName() {
        return myMethodName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MethodMetric metric = (MethodMetric) o;

        if (!getMethodName().equals(metric.getMethodName())) return false;
        if (!getTotalTime().equals(metric.getTotalTime())) return false;
        return getOwnTime().equals(metric.getOwnTime());

    }

    @Override
    public int hashCode() {
        int result = getMethodName().hashCode();
        result = 31 * result + getTotalTime().hashCode();
        result = 31 * result + getOwnTime().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MethodMetric{" +
                "MethodName='" + getMethodName() + '\'' +
                ", TotalTime='" + getTotalTime() + '\'' +
                ", OwnTime='" + getOwnTime() + '\'' +
                '}';
    }
}
