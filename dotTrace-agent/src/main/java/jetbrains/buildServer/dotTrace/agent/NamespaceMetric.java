package jetbrains.buildServer.dotTrace.agent;


import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class NamespaceMetric extends MetricBase {
    private final String namespaceName;
    private final int minMethodTime;
    @NotNull
    private final List<String> ignoreMethods;

    public NamespaceMetric(@NotNull final String namespaceName, @NotNull String myTotalTime, @NotNull String myOwnTime, final int minMethodTime, @NotNull final List<String> ignoreMethods) {
        super(myTotalTime, myOwnTime);
        this.namespaceName = namespaceName;
        this.minMethodTime = minMethodTime;
        this.ignoreMethods = Collections.unmodifiableList(ignoreMethods);
    }

    @NotNull
    public List<String> getIgnoreMethods() {
        return ignoreMethods;
    }

    @NotNull
    public String getNamespaceName() {
        return namespaceName;
    }

    public int getMinMethodTotalTime() {
        return minMethodTime;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final NamespaceMetric metric = (NamespaceMetric) o;
        if (!getNamespaceName().equals(metric.getNamespaceName())) return false;
        if (!getTotalTime().equals(metric.getTotalTime())) return false;
        if (!getOwnTime().equals(metric.getOwnTime())) return false;
        return getMinMethodTotalTime() == metric.getMinMethodTotalTime();
    }

    @Override
    public int hashCode() {
        int result = getNamespaceName().hashCode();
        result = 31 * result + getTotalTime().hashCode();
        result = 31 * result + getOwnTime().hashCode();
        result = 31 * result + getMinMethodTotalTime();
        return result;
    }

    @Override
    public String toString() {
        return "NamespaceMetric{" +
                "NamespaceName='" + getNamespaceName() + '\'' +
                ", TotalTime='" + getTotalTime() + '\'' +
                ", OwnTime='" + getOwnTime() + '\'' +
                '}';
    }

}
