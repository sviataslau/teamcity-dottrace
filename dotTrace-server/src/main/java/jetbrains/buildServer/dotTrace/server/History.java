package jetbrains.buildServer.dotTrace.server;

import jetbrains.buildServer.serverSide.SFinishedBuild;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface History {
    @NotNull
    Iterable<HistoryElement> getElements(@NotNull final List<SFinishedBuild> builds);
}
