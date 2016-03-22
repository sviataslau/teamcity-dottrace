package jetbrains.buildServer.dotTrace.agent;


import org.jetbrains.annotations.NotNull;

public class NamespaceMetric extends MetricBase {
    private String namespaceName;

    public NamespaceMetric(@NotNull final String namespaceName, @NotNull String myTotalTime, @NotNull String myOwnTime) {
        super(myTotalTime, myOwnTime);
        this.namespaceName = namespaceName;
    }


    @NotNull
    public String getNamespaceName() {
        return namespaceName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final NamespaceMetric metric = (NamespaceMetric) o;
        if (!getNamespaceName().equals(metric.getNamespaceName())) return false;
        if (!getTotalTime().equals(metric.getTotalTime())) return false;
        return getOwnTime().equals(metric.getOwnTime());

    }

    @Override
    public int hashCode() {
        int result = getNamespaceName().hashCode();
        result = 31 * result + getTotalTime().hashCode();
        result = 31 * result + getOwnTime().hashCode();
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
