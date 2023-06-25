package com.denisgruiax.blockchaintradingbot.application.mainactivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.binance.connector.client.impl.SpotClientImpl;
import com.denisgruiax.blockchaintradingbot.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.denisgruiax.blockchaintradingbot.databinding.ActivityMainBinding;
import com.binance.connector.client.SpotClient;
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String apiKey;
    private String secretKey;
    private TextView apiKeyText;
    private TextView secretKeyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_wallet_statistics, R.id.nav_bot_strategy, R.id.nav_charts)
                .setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);

        apiKeyText = headerView.findViewById(R.id.textView1);
        secretKeyText = headerView.findViewById(R.id.textView2);

        getKeysFromIntent();

        apiKeyText.setText(apiKey);
        secretKeyText.setText(secretKey);
    }

    private void getKeysFromIntent() {
        Bundle bundle = getIntent().getExtras();
        apiKey = bundle.getString("apiKey");
        secretKey = bundle.getString("secretKey");
    }

    private void setTextFields() {
        apiKeyText.setText(apiKey);
        secretKeyText.setText(secretKey);
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
         * String storedMnemonic = sharedPreferences.getString("mnemonic", null); if
         * (storedMnemonic != null) { mnemonic = storedMnemonic;
         * 
         * try { Wallet wallet = Wallet.deriveFromMnemonic(mnemonic, 0);
         * walletName.setText(wallet.toString()); } catch
         * (Exceptions.CannotDeriveKeysException cannotDeriveKeysException) {
         * toastLongMessage("Error log into wallet!"); } }
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void toastLongMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
