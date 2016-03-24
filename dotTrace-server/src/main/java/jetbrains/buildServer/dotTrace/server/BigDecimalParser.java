package jetbrains.buildServer.dotTrace.server;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public interface BigDecimalParser {
    @Nullable
    BigDecimal tryParseBigDecimal(@Nullable final String valueStr);
}
