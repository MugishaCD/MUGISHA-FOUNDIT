package com.foundit.dto;

public class DashboardStatsDTO {
    private long totalLost;
    private long totalFound;
    private long totalMatches;
    private long pendingClaims;

    public DashboardStatsDTO(long totalLost, long totalFound, long totalMatches, long pendingClaims) {
        this.totalLost = totalLost;
        this.totalFound = totalFound;
        this.totalMatches = totalMatches;
        this.pendingClaims = pendingClaims;
    }

    public long getTotalLost() { return totalLost; }
    public long getTotalFound() { return totalFound; }
    public long getTotalMatches() { return totalMatches; }
    public long getPendingClaims() { return pendingClaims; }
}
