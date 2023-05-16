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
import com.denisgruiax.blockchaintradingbot.activities.MainActivity;
import multiversx.Exceptions.CannotDeriveKeysException;
import multiversx.Wallet;

public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String mnemonic;
    Wallet wallet;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        EditText mnemonicText = findViewById(R.id.mnemonicText);
        Button confirmButton = findViewById(R.id.buttonLogin);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        confirmButton.setOnClickListener(view -> {
            try {
                //mnemonic = mnemonicText.getText().toString();
                mnemonic = "emotion spare multiply lecture rude machine raise radio ability doll depend equip pass ghost cabin delay birth opera shoe force any slow fluid old";
                wallet = Wallet.deriveFromMnemonic(mnemonic, 0);

                putMnemonicInMemmory("mnemonic", mnemonic);
                // toastLongMessage(wallet.toString());

                if (mnemonic.length() > 10) {
                    Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    mainActivity.putExtra("mnemonic", mnemonic);

                    startActivity(mainActivity);
                }
            } catch (CannotDeriveKeysException cannotDeriveKeysException) {
                toastLongMessage("Error log into wallet!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String storedMnemonic = sharedPreferences.getString("mnemonic", null);

        if (storedMnemonic != null) {
            mnemonic = storedMnemonic;
            try {
                wallet = Wallet.deriveFromMnemonic(mnemonic, 0);

                // toastLongMessage(mnemonic);
                if (mnemonic.length() > 10) {
                    Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    mainActivity.putExtra("mnemonic", mnemonic);
                    startActivity(mainActivity);
                }

                finish(); // Optional: Finish the login activity
            } catch (CannotDeriveKeysException cannotDeriveKeysException) {
                toastLongMessage("Error log into wallet!");
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("mnemonic", mnemonic);

        super.onSaveInstanceState(outState);
    }

    private void putMnemonicInMemmory(String key, String mnemonic) {
        editor = sharedPreferences.edit();
        editor.putString(key, mnemonic);
        editor.apply();
    }

    private void toastLongMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
