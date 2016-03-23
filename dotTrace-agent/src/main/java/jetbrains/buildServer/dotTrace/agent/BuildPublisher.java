package jetbrains.buildServer.dotTrace.agent;

import com.intellij.openapi.util.text.StringUtil;
import jetbrains.buildServer.dotNet.buildRunner.agent.*;
import jetbrains.buildServer.dotTrace.Constants;
import jetbrains.buildServer.dotTrace.StatisticMessage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BuildPublisher implements ResourcePublisher {
  private final TextParser<List<MethodMetric>> myReportParser;
  private final TextParser<Threshold> myThresholdsParser;
  private final ResourcePublisher myAfterBuildPublisher;
  private final RunnerParametersService myParametersService;
  private final FileService myFileService;
  private final LoggerService myLoggerService;

  public BuildPublisher(
          @NotNull final TextParser<List<MethodMetric>> reportParser,
          @NotNull final TextParser<Threshold> thresholdsParser,
          @NotNull final ResourcePublisher afterBuildPublisher,
          @NotNull final RunnerParametersService parametersService,
          @NotNull final FileService fileService,
          @NotNull final LoggerService loggerService) {
    myReportParser = reportParser;
    myThresholdsParser = thresholdsParser;
    myAfterBuildPublisher = afterBuildPublisher;
    myParametersService = parametersService;
    myFileService = fileService;
    myLoggerService = loggerService;
  }

  @Override
  public void publishBeforeBuildFile(@NotNull final CommandLineExecutionContext commandLineExecutionContext, @NotNull final File file, @NotNull final String content) {
  }

  @Override
  public void publishAfterBuildArtifactFile(@NotNull final CommandLineExecutionContext commandLineExecutionContext, @NotNull final File reportFile) {
    myAfterBuildPublisher.publishAfterBuildArtifactFile(commandLineExecutionContext, reportFile);

    String thresholdsStr = myParametersService.tryGetRunnerParameter(Constants.THRESHOLDS_VAR);
    Threshold thresholdValues;
    if(!StringUtil.isEmptyOrSpaces(thresholdsStr)) {
      thresholdValues = myThresholdsParser.parse(thresholdsStr);
    }
    else {
      return;
    }

    try {
      final String reportContent = myFileService.readAllTextFile(reportFile);
      final List<MethodMetric> measuredValues = myReportParser.parse(reportContent);

      for (MethodMetric measuredValue : measuredValues) {
        final MetricBase thresholdValue = thresholdValues.getMetric(measuredValue.getMethodName());
        if (thresholdValue == null) {
          continue;
        }
        if (thresholdValue instanceof NamespaceMetric) {
          NamespaceMetric m = (NamespaceMetric) thresholdValue;
          int minMethodTime = m.getMinMethodTotalTime();
          if (minMethodTime > 0) {
            try {
              int measuredTotalTime = Integer.parseInt(measuredValue.getTotalTime());
              if (measuredTotalTime < minMethodTime)
                continue;
            } catch (NumberFormatException e) {
              continue;
            }
          }
        }
        myLoggerService.onMessage(new StatisticMessage(measuredValue.getMethodName(), thresholdValue.getTotalTime(), thresholdValue.getOwnTime(), measuredValue.getTotalTime(), measuredValue.getOwnTime()));
      }
    }
    catch (IOException e) {
      throw new BuildException(e.getMessage());
    }
  }

}
