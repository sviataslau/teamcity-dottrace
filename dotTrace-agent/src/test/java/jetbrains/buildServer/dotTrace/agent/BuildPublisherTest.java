package jetbrains.buildServer.dotTrace.agent;

import jetbrains.buildServer.dotNet.buildRunner.agent.*;
import jetbrains.buildServer.dotTrace.Constants;
import jetbrains.buildServer.dotTrace.StatisticMessage;
import org.jetbrains.annotations.NotNull;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BuildPublisherTest {
  private Mockery myCtx;
  private FileService myFileService;
  private TextParser<List<MethodMetric>> myReportParser;
  private ResourcePublisher myAfterBuildPublisher;
  private LoggerService myLoggerService;
  private TextParser<Threshold> myThresholdsParser;
  private RunnerParametersService myRunnerParametersService;

  @BeforeMethod
  public void setUp()
  {
    myCtx = new Mockery();

    //noinspection unchecked
    myReportParser = (TextParser<List<MethodMetric>>) myCtx.mock(TextParser.class, "ReportParser");
    //noinspection unchecked
    myThresholdsParser = (TextParser<Threshold>) myCtx.mock(TextParser.class, "ThresholdsParser");
    myAfterBuildPublisher = myCtx.mock(ResourcePublisher.class);
    myRunnerParametersService = myCtx.mock(RunnerParametersService.class);
    myFileService = myCtx.mock(FileService.class);
    myLoggerService = myCtx.mock(LoggerService.class);
  }

  @Test
  public void shouldPublishAfterBuildArtifactFile() {
    // Given
    final File reportFile = new File("report");
    final List<MethodMetric> reportMetrics =
      Arrays.asList(
              new MethodMetric("Method1", "100", "33"),
              new MethodMetric("Method77", "123", "456"),
              new MethodMetric("Method2", "99", "22"),
              new MethodMetric("Namespace.One.Method", "1", "2"));

    final Threshold thresholdsMetrics = new Threshold(
      Arrays.asList(
              new MethodMetric("Method88", "88", "345"),
              new MethodMetric("Method1", "100", "1000"),
              new MethodMetric("Method2", "F22", "F35"),
              new NamespaceMetric("Namespace.One", "F1", "F2")));

    final CommandLineExecutionContext ctx = new CommandLineExecutionContext(0);

    myCtx.checking(new Expectations() {{
      oneOf(myAfterBuildPublisher).publishAfterBuildArtifactFile(ctx, reportFile);

      oneOf(myRunnerParametersService).tryGetRunnerParameter(Constants.THRESHOLDS_VAR);
      will(returnValue("thresholds"));

      oneOf(myThresholdsParser).parse("thresholds");
      will(returnValue(thresholdsMetrics));

      //noinspection EmptyCatchBlock
      try {
        oneOf(myFileService).readAllTextFile(reportFile);
      }
      catch (IOException e) {
      }

      will(returnValue("report's content"));

      oneOf(myReportParser).parse("report's content");
      will(returnValue(reportMetrics));

      oneOf(myLoggerService).onMessage(new StatisticMessage("Method1", "100", "1000", "100", "33"));
      oneOf(myLoggerService).onMessage(new StatisticMessage("Method2", "F22", "F35", "99", "22"));
      oneOf(myLoggerService).onMessage(new StatisticMessage("Namespace.One.Method", "F1", "F2", "1", "2"));
    }});

    final ResourcePublisher instance = createInstance();

    // When
    instance.publishAfterBuildArtifactFile(ctx, reportFile);

    // Then
    myCtx.assertIsSatisfied();
  }

  @NotNull
  private ResourcePublisher createInstance()
  {
    return new BuildPublisher(
      myReportParser,
      myThresholdsParser,
      myAfterBuildPublisher,
      myRunnerParametersService,
      myFileService,
      myLoggerService);
  }
}
