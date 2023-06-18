package com.denisgruiax.blockchaintradingbot.domains.threads;

import android.util.Log;

import com.denisgruiax.blockchaintradingbot.domains.botfactory.Bot;

public class Trading extends Thread {
    private volatile boolean running = true;
    private Bot bot;

    public Trading(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        while (running) {
            bot.executeStrategy();

            try {
                Thread.sleep(1000 * 60 * 5);
            } catch (InterruptedException interruptedException) {
                throw new RuntimeException();
            }
        }
    }

    public void stopTrading() {
        running = false;
    }
}
