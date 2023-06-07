package com.denisgruiax.blockchaintradingbot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.denisgruiax.blockchaintradingbot.application.mainactivity.MainActivity;
import com.denisgruiax.blockchaintradingbot.utils.Keys;

public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferences sharedPreferences;
    private String apiKey;
    private String secretKey;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        EditText apiKeyText = findViewById(R.id.apiKeyText);
        EditText secretKeyText = findViewById(R.id.secretKeyText);

        Button loginButton = findViewById(R.id.buttonLogin);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        loginButton.setOnClickListener(view -> {
            //apiKey = apiKeyText.getText().toString();
            apiKey = Keys.getApiKey();

            //secretKey = secretKeyText.getText().toString();
            secretKey = Keys.getSecretKey();

            saveDictionaryInMemmory("apiKey", apiKey);
            saveDictionaryInMemmory("secretKey", secretKey);
            // toastLongMessage(wallet.toString());

            Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
            mainActivity.putExtra("apiKey", apiKey);
            mainActivity.putExtra("secretKey", secretKey);

            startActivity(mainActivity);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String storedApiKey = sharedPreferences.getString("apiKey", null);
        String storedSecretKey = sharedPreferences.getString("secretKey", null);

        if ((storedApiKey != null) && (storedSecretKey != null)) {
            apiKey = storedApiKey;
            secretKey = storedSecretKey;

            Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
            mainActivity.putExtra("apiKey", apiKey);
            mainActivity.putExtra("secretKey", secretKey);

            startActivity(mainActivity);

            finish(); // Optional: Finish the login activity
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("apiKey", apiKey);

        super.onSaveInstanceState(outState);
    }

    private void saveDictionaryInMemmory(String key, String apiKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, apiKey);
        editor.apply();
    }

    private void toastLongMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
