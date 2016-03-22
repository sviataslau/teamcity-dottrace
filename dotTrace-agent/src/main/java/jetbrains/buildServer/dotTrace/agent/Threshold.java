package jetbrains.buildServer.dotTrace.agent;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Threshold {
  private final List<MetricBase> myMetrics;

  public Threshold(@NotNull final List<MetricBase> metrics) {
    myMetrics = Collections.unmodifiableList(metrics);
  }

  @NotNull
  public List<MetricBase> getMetrics() {
    return myMetrics;
  }

  public MetricBase getMetric(@NotNull String method) {
    for (MetricBase mm : getMetrics()) {
      if (mm instanceof MethodMetric) {
        MethodMetric methodMetric = (MethodMetric) mm;
        if (methodMetric.getMethodName().equalsIgnoreCase(method))
          return mm;
      }
    }
    for (MetricBase nm : getMetrics()) {
      if (nm instanceof NamespaceMetric) {
        NamespaceMetric namespaceMetric = (NamespaceMetric) nm;
        if (method.startsWith(namespaceMetric.getNamespaceName()))
          return nm;
      }
    }
    return null;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Threshold metrics = (Threshold) o;

    return getMetrics().equals(metrics.getMetrics());
  }

  @Override
  public int hashCode() {
    return getMetrics().hashCode();
  }
}
