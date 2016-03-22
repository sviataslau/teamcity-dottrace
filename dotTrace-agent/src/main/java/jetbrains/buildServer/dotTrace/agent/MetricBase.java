package jetbrains.buildServer.dotTrace.agent;


import org.jetbrains.annotations.NotNull;

public abstract class MetricBase {
    private final String myTotalTime;
    private final String myOwnTime;

    public MetricBase(String myTotalTime, String myOwnTime) {
        this.myTotalTime = myTotalTime;
        this.myOwnTime = myOwnTime;
    }

    @NotNull
    public String getTotalTime() {
        return myTotalTime;
    }

    @NotNull
    public String getOwnTime() {
        return myOwnTime;
    }
}
