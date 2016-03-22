package jetbrains.buildServer.dotTrace.agent;

import jetbrains.buildServer.dotNet.buildRunner.agent.TextParser;
import jetbrains.buildServer.dotNet.buildRunner.agent.XmlDocumentManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

public class ReportParserTest {
  private static final String ourXmlSample =
          "<Report>\n" +
                  "<Function Id=\"0x01200007\" FQN=\"HelloWorld.Tests.UnitTest4.TestMethodCatM01\" TotalTime=\"26\" OwnTime=\"10\" Calls=\"4\" Instances=\"1\"/>\n" +
                  "<Function Id=\"0x01200008\" FQN=\"HelloWorld.Tests.UnitTest4.TestMethodCatM02\" TotalTime=\"334\" OwnTime=\"0\" Calls=\"1\" Instances=\"10\"/>\n" +
                  "<Function Id=\"0x01200009\" FQN=\"HelloWorld.Tests.UnitTest4.TestMethodCatM03\" TotalTime=\"100\" OwnTime=\"0\" Calls=\"18\" Instances=\"1\"/>\n" +
                  "</Report>";
  private static final String ourXmlSample2 =
          "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
                  "<Report>\n" +
                  "<Function Id=\"0x01200007\" FQN=\"HelloWorld.Tests.UnitTest4.TestMethodCatM01\" TotalTime=\"26\" OwnTime=\"10\" Calls=\"4\" Instances=\"1\"/>\n" +
                  "<Function Id=\"0x01200008\" FQN=\"HelloWorld.Tests.UnitTest4.TestMethodCatM02\" TotalTime=\"334\" OwnTime=\"0\" Calls=\"1\" Instances=\"10\"/>\n" +
                  "<Function Id=\"0x01200009\" FQN=\"HelloWorld.Tests.UnitTest4.TestMethodCatM03\" TotalTime=\"100\" OwnTime=\"0\" Calls=\"18\" Instances=\"1\"/>\n" +
                  "</Report>";

  @DataProvider(name = "parseDotTraceReportFromXmlCases")
  public Object[][] getParseDotTraceReportFromXmlCases() {
    return new Object[][] {
      {
        ourXmlSample,
          Arrays.asList(
                  new MethodMetric("HelloWorld.Tests.UnitTest4.TestMethodCatM01", "26", "10"),
                  new MethodMetric("HelloWorld.Tests.UnitTest4.TestMethodCatM02", "334", "0"),
                  new MethodMetric("HelloWorld.Tests.UnitTest4.TestMethodCatM03", "100", "0"))},
      {
        ourXmlSample2,
          Arrays.asList(
                  new MethodMetric("HelloWorld.Tests.UnitTest4.TestMethodCatM01", "26", "10"),
                  new MethodMetric("HelloWorld.Tests.UnitTest4.TestMethodCatM02", "334", "0"),
                  new MethodMetric("HelloWorld.Tests.UnitTest4.TestMethodCatM03", "100", "0"))},
      {
        "",
              (Collections.<MethodMetric>emptyList())},
    };
  }

  @Test(dataProvider = "parseDotTraceReportFromXmlCases")
  public void shouldParseDotTraceReportFromXml(@NotNull final String text, @NotNull final List<MethodMetric> expectedMetrics)
  {
    final TextParser<List<MethodMetric>> instance = createInstance();

    // When
    final List<MethodMetric> actualMetrics = instance.parse(text);

    // Then
    then(actualMetrics).isEqualTo(expectedMetrics);
  }

  @NotNull
  private TextParser<List<MethodMetric>> createInstance()
  {
    return new ReportParser(new XmlDocumentManagerImpl());
  }
}
