package jetbrains.buildServer.dotTrace.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public interface HistoryElement {
    @Nullable
    BigDecimal tryGetValue(@NotNull final String key);
}
