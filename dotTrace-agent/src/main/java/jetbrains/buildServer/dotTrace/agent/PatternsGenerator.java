package jetbrains.buildServer.dotTrace.agent;

import com.intellij.openapi.util.text.StringUtil;
import jetbrains.buildServer.dotNet.buildRunner.agent.ResourceGenerator;
import jetbrains.buildServer.dotNet.buildRunner.agent.RunnerParametersService;
import jetbrains.buildServer.dotNet.buildRunner.agent.TextParser;
import jetbrains.buildServer.dotNet.buildRunner.agent.XmlDocumentManager;
import jetbrains.buildServer.dotTrace.Constants;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collections;

public class PatternsGenerator implements ResourceGenerator<Context> {
  private static final String PATTERNS_ELEMENT = "Patterns";
  private static final String PATTERN_ELEMENT = "Pattern";

  private final TextParser<Threshold> myThresholdsParser;
  private final RunnerParametersService myParametersService;
  private final XmlDocumentManager myDocumentManager;

  public PatternsGenerator(
          @NotNull final TextParser<Threshold> thresholdsParser,
          @NotNull final RunnerParametersService parametersService,
          @NotNull final XmlDocumentManager documentManager) {
    myThresholdsParser = thresholdsParser;
    myParametersService = parametersService;
    myDocumentManager = documentManager;
  }

  @NotNull
  private static Element createSimpleElement(@NotNull final Document doc, @NotNull final String name, @NotNull final String value) {
    Element executableElement = doc.createElement(name);
    executableElement.setTextContent(value);
    return executableElement;
  }

  @NotNull
  @Override
  public String create(@NotNull final Context context) {
    final Document doc = myDocumentManager.createDocument();
    final Element patternsElement = doc.createElement(PATTERNS_ELEMENT);
    String thresholdsStr = myParametersService.tryGetRunnerParameter(Constants.THRESHOLDS_VAR);
    if(!StringUtil.isEmptyOrSpaces(thresholdsStr)) {
      final Element patternElement = createSimpleElement(doc, PATTERN_ELEMENT, "");
      patternsElement.appendChild(patternElement);
    }

    doc.appendChild(patternsElement);
    return myDocumentManager.convertDocumentToString(doc, Collections.<String, String>emptyMap());
  }
}
