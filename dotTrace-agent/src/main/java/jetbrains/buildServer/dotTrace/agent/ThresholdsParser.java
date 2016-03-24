package jetbrains.buildServer.dotTrace.agent;

import com.intellij.openapi.util.text.StringUtil;
import jetbrains.buildServer.dotNet.buildRunner.agent.BuildException;
import jetbrains.buildServer.dotNet.buildRunner.agent.TextParser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThresholdsParser implements TextParser<Threshold> {
    public static final String LINE_SEPARATOR = "\n";
    private static final String ARG_SEPARATOR = " ";
    private static final String IGNORE_LIST_SEPARATOR = ",";

    @NotNull
    @Override
    public Threshold parse(@NotNull final String text) {
        final List<String> lines = StringUtil.split(text, LINE_SEPARATOR);
        List<MetricBase> metrics = new ArrayList<MetricBase>(lines.size());
        for (String line : lines) {
            if (StringUtil.isEmptyOrSpaces(line)) {
                continue;
            }

            final String[] params = line.trim().split(ARG_SEPARATOR);
            if (params.length >= 3) {
                String target = params[0];
                MetricBase metric;
                final String namespacePrefix = "N:";
                if (target.startsWith(namespacePrefix)) {
                    try {
                        int minMethodTime = params.length > 3 ? Integer.parseInt(params[3]) : 0;
                        List<String> ignoreMethods = params.length > 4 ? Arrays.asList(params[4].split(IGNORE_LIST_SEPARATOR)) : Collections.<String>emptyList();
                        metric = new NamespaceMetric(target.replace(namespacePrefix, ""), params[1], params[2], minMethodTime, ignoreMethods);
                    } catch (NumberFormatException e) {
                        throw new BuildException("Invalid metrics");
                    }
                } else {
                    metric = new MethodMetric(target, params[1], params[2]);
                }
                metrics.add(metric);
            } else {
                throw new BuildException("Invalid metrics");
            }
        }

        return new Threshold(metrics);
    }
}
