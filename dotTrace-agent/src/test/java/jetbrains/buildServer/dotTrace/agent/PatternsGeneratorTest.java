package jetbrains.buildServer.dotTrace.agent;

import jetbrains.buildServer.dotNet.buildRunner.agent.*;
import jetbrains.buildServer.dotTrace.Constants;
import org.jetbrains.annotations.NotNull;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Collections;

import static org.assertj.core.api.BDDAssertions.then;

public class PatternsGeneratorTest {
  private Mockery myCtx;
  private TextParser<Threshold> myReportPatternsParser;
  private RunnerParametersService myRunnerParametersService;

  @BeforeMethod
  public void setUp()
  {
    myCtx = new Mockery();

    //noinspection unchecked
    myReportPatternsParser = (TextParser<Threshold>) myCtx.mock(TextParser.class);
    myRunnerParametersService = myCtx.mock(RunnerParametersService.class);
  }

  @Test
  public void shouldGenerateContent() {
    // Given
    String expectedContent = "<Patterns>" +
            "<Pattern/>" +
            "</Patterns>";

    final CommandLineSetup setup = new CommandLineSetup("tool", Collections.<CommandLineArgument>emptyList(), Collections.<CommandLineResource>emptyList());
    myCtx.checking(new Expectations() {{
      oneOf(myRunnerParametersService).tryGetRunnerParameter(Constants.THRESHOLDS_VAR);
      will(returnValue("thresholds"));
    }});

    final PatternsGenerator instance = createInstance();

    // When
    final String content = instance.create(new Context(setup, new File("a"), new File("b"), new File("c"), new File("s")));

    // Then
    myCtx.assertIsSatisfied();
    then(content.trim().replace("\n", "").replace("\r", "")).isEqualTo(expectedContent.trim().replace("\n", "").replace("\r", ""));
  }

  @NotNull
  private PatternsGenerator createInstance()
  {
    return new PatternsGenerator(
      myReportPatternsParser,
      myRunnerParametersService,
      new XmlDocumentManagerImpl());
  }
}