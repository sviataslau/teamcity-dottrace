package jetbrains.buildServer.dotTrace.server;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public class BigDecimalParserImpl implements BigDecimalParser {
    @Nullable
    @Override
    public BigDecimal tryParseBigDecimal(@Nullable final String valueStr) {
        if (StringUtil.isEmptyOrSpaces(valueStr)) {
            return null;
        }

        try {
            return new BigDecimal(valueStr);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
