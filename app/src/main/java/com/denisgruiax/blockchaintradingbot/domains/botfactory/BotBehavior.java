package com.denisgruiax.blockchaintradingbot.domains.botfactory;

public enum BotBehavior {
    AGGRESSIVE("Aggressive"),
    RISKY("Risky"),
    MODERATE("Moderate"),
    CONSERVATIVE("Conservative");

    private final String displayName;

    BotBehavior(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}