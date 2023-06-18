package com.denisgruiax.blockchaintradingbot.domains.botfactory;

public enum BotBehavior {
    AGGRESSIVE("Aggressive Bot"),
    RISKY("Risky Bot"),
    MODERATE("Moderate Bot"),
    CONSERVATIVE("Conservative Bot");

    private final String displayName;

    BotBehavior(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}