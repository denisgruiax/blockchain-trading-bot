package com.denisgruiax.blockchaintradingbot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.denisgruiax.blockchaintradingbot.application.mainactivity.MainActivity;
import com.denisgruiax.blockchaintradingbot.data.remote.binanceapi.spottrade.FetchBinanceAccount;
import com.denisgruiax.blockchaintradingbot.data.remote.binanceapi.spottrade.FetchCryptoBalance;
import com.denisgruiax.blockchaintradingbot.data.remote.coingeckoapi.fetchprice.FetchPrice;
import com.denisgruiax.blockchaintradingbot.utils.Keys;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private AppBarConfiguration appBarConfiguration;
    private SharedPreferences sharedPreferences;
    private String apiKey;
    private String secretKey;
    private Toast toastMessage;

    private EditText textViewApiKey;
    private EditText textViewSecretKey;
    private Button buttonLogin;

    private ExecutorService executorService = new ThreadPoolExecutor(1, 10, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    ;
    private Runnable runnable;

    private BinanceApiClientFactory binanceApiClientFactory;
    private BinanceApiRestClient client;
    private Future<Account> futureBinanceAccount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initializeUIElements();

        // apiKey = apiKeyText.getText().toString();
        apiKey = Keys.getApiKey();

        // secretKey = secretKeyText.getText().toString();
        secretKey = Keys.getSecretKey();

        fetchFutureBinanceAccount();

        onLoginButtonPress();
        fetchData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*getApiKeysFromMemory();

        if ((apiKey != null) && (secretKey != null)) {
            startMyMainActivity();

            finish();
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("apiKey", apiKey);

        super.onSaveInstanceState(outState);
    }

    private void initializeUIElements() {
        textViewApiKey = findViewById(R.id.apiKeyText);
        textViewApiKey = findViewById(R.id.secretKeyText);

        buttonLogin = findViewById(R.id.buttonLogin);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void saveDictionaryInMemmory(String key, String apiKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, apiKey);
        editor.apply();
    }

    private void getApiKeysFromMemory() {
        apiKey = sharedPreferences.getString("apiKey", null);
        secretKey = sharedPreferences.getString("secretKey", null);
    }

    private void toastLongMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void startMyMainActivity() {
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);

        mainActivity.putExtra("apiKey", apiKey);
        mainActivity.putExtra("secretKey", secretKey);

        startActivity(mainActivity);
    }

    private void fetchFutureBinanceAccount() {
        Log.e("apiKey", apiKey);
        Log.e("secretKey", secretKey);

        binanceApiClientFactory = BinanceApiClientFactory.newInstance(apiKey, secretKey);

        futureBinanceAccount = executorService.submit(new FetchBinanceAccount(binanceApiClientFactory));
    }

    private Account getAccountFromFutureAccount() {
        try {
            if (futureBinanceAccount != null)
                if (futureBinanceAccount.isDone()) {
                    return futureBinanceAccount.get();
                }
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }

        return null;
    }

    private void onLoginButtonPress() {
        buttonLogin.setOnClickListener(view -> {
            saveDictionaryInMemmory("apiKey", apiKey);
            saveDictionaryInMemmory("secretKey", secretKey);

            if (getAccountFromFutureAccount() != null)
                startMyMainActivity();
            else toastLongMessage("Wrong API keys!");
        });
    }

    private void fetchData() {
        runnable = new Runnable() {
            @Override
            public void run() {
                getAccountFromFutureAccount();
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
    }
}
