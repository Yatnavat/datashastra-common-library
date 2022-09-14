package tech.oorjaa.reporting.models;

import lombok.Getter;

@Getter
public class MemoryStatus {
    private final long maxMemory;
    private final long usedMemory;
    private final long totalMemory;
    private final long freeMemory;

    public MemoryStatus() {
        maxMemory = getMaxMemory();
        usedMemory = getUsedMemory();
        totalMemory = getTotalMemory();
        freeMemory = getFreeMemory();
    }

    public long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public long getUsedMemory() {
        return getMaxMemory() - getFreeMemory();
    }

    public long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }
}
