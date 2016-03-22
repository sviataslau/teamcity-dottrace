package jetbrains.buildServer.dotTrace.agent;

import jetbrains.buildServer.dotNet.buildRunner.agent.BuildException;
import jetbrains.buildServer.dotNet.buildRunner.agent.TextParser;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.BDDAssertions.then;

public class MetricsParserTest {
  private static final String ourlineSeparator = "\n";

  @DataProvider(name = "parseThresholdsFromStringCases")
  public Object[][] getParseThresholdsFromStringCases() {
    return new Object[][] {
            {"IntegrationTests.MainTests.Test1 100 F15", new Threshold(Collections.singletonList((MetricBase) new MethodMetric("IntegrationTests.MainTests.Test1", "100", "F15"))), false},
            {"IntegrationTests.MainTests.Test1 100 F15" + ourlineSeparator + "IntegrationTests.MainTests.Test2 200 A15", new Threshold(Arrays.asList((MetricBase) new MethodMetric("IntegrationTests.MainTests.Test1", "100", "F15"), new MethodMetric("IntegrationTests.MainTests.Test2", "200", "A15"))), false},
            {"   " + ourlineSeparator + "IntegrationTests.MainTests.Test1 100 F15" + ourlineSeparator + "   " + ourlineSeparator + ourlineSeparator + "IntegrationTests.MainTests.Test2 200 A15" + ourlineSeparator, new Threshold(Arrays.asList((MetricBase) new MethodMetric("IntegrationTests.MainTests.Test1", "100", "F15"), new MethodMetric("IntegrationTests.MainTests.Test2", "200", "A15"))), false},
            {"", new Threshold(Collections.<MetricBase>emptyList()), false},
            {"IntegrationTests.MainTests.Test1 100 F15 300", new Threshold(Collections.<MetricBase>emptyList()), true},
            {"N:Namespace.One 100 F15", new Threshold(Collections.singletonList((MetricBase) new NamespaceMetric("Namespace.One", "100", "F15"))), false},
            {"IntegrationTests.MainTests.Test1", new Threshold(Collections.<MetricBase>emptyList()), true},
            {"IntegrationTests.MainTests.Test1" + ourlineSeparator + "IntegrationTests.MainTests.Test2 200 A15", new Threshold(Collections.<MetricBase>emptyList()), true},
    };
  }

  @Test(dataProvider = "parseThresholdsFromStringCases")
  public void shouldParseThresholdsFromString(@NotNull final String text, @NotNull final Threshold expectedMetrics, boolean expectedExceptionThrown)
  {
    // Given
    final TextParser<Threshold> instance = createInstance();

    // When
    Threshold actualMetrics = null;
    boolean actualExceptionThrown = false;

    try {
      actualMetrics = instance.parse(text);
    }
    catch (BuildException ex) {
      actualExceptionThrown = true;
    }

    // Then
    if(!expectedExceptionThrown) {
      then(actualMetrics.getMetrics()).isEqualTo(expectedMetrics.getMetrics());
    }

    then(actualExceptionThrown).isEqualTo(expectedExceptionThrown);
  }

  @NotNull
  private TextParser<Threshold> createInstance()
  {
    return new ThresholdsParser();
  }
}