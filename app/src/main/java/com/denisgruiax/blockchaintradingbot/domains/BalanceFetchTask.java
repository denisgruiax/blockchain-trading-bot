package com.denisgruiax.blockchaintradingbot.domains;

import android.os.AsyncTask;

public class BalanceFetchTask extends AsyncTask<Void, Void, String> {

    private BalanceUpdateListener listener;

    public BalanceFetchTask(BalanceUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        // Replace this with your actual balance fetching logic
        try {
            Thread.sleep(5000); // Simulate fetching balance from the server
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "1000"; // Replace with the actual balance value
    }

    @Override
    protected void onPostExecute(String balance) {
        if (listener != null) {
            listener.onBalanceUpdated(balance);
        }
    }

    public interface BalanceUpdateListener {
        void onBalanceUpdated(String balance);
    }
}
