package jetbrains.buildServer.dotTrace.agent;

import jetbrains.buildServer.dotNet.buildRunner.agent.BuildException;
import jetbrains.buildServer.dotNet.buildRunner.agent.TextParser;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

public class ProcessFilterParserTest {
    private static final String LINE_SEPARATOR = "\n";

    @DataProvider(name = "parseFiltersFromStringCases")
    public Object[][] getParseFiltersFromStringCases() {
        return new Object[][]{
                {"*service", Arrays.asList(new ProcessFilter("*service")), false},
                {"", Arrays.asList(), false},
                {"*service" + LINE_SEPARATOR + "Abc*.exe", Arrays.asList(new ProcessFilter("*service"), new ProcessFilter("Abc*.exe")), false},
                {"   " + LINE_SEPARATOR + "*service" + LINE_SEPARATOR + LINE_SEPARATOR + "   " + LINE_SEPARATOR + "Abc*.exe" + LINE_SEPARATOR, Arrays.asList(new ProcessFilter("*service"), new ProcessFilter("Abc*.exe")), false},
        };
    }

    @Test(dataProvider = "parseFiltersFromStringCases")
    public void shouldParseFiltersFromString(@NotNull final String text, @NotNull final List<ProcessFilter> expectedFilters, boolean expectedExceptionThrown) {
        // Given
        final TextParser<List<ProcessFilter>> instance = createInstance();

        // When
        List<ProcessFilter> actualFilters = null;
        boolean actualExceptionThrown = false;

        try {
            actualFilters = instance.parse(text);
        } catch (BuildException ex) {
            actualExceptionThrown = true;
        }

        // Then
        if (!expectedExceptionThrown) {
            //noinspection ConstantConditions,ConstantConditions
            then(actualFilters).containsExactlyElementsOf(expectedFilters);
        }

        then(actualExceptionThrown).isEqualTo(expectedExceptionThrown);
    }

    @NotNull
    private TextParser<List<ProcessFilter>> createInstance() {
        return new ProcessFilterParser();
    }
}
