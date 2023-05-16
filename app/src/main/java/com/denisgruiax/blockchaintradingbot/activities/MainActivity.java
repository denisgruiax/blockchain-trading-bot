package com.denisgruiax.blockchaintradingbot.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

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

import org.bouncycastle.util.encoders.DecoderException;
import org.bouncycastle.util.encoders.Hex;
import org.bitcoinj.core.Bech32;

import java.util.Base64;

import multiversx.Address;
import multiversx.Exceptions;
import multiversx.Wallet;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String mnemonic;
    private Wallet wallet;
    private Address address;
    private TextView walletName;
    private TextView publicKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null)
                        .show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle bundle = getIntent().getExtras();
        mnemonic = bundle.getString("mnemonic");

        try {
            wallet = Wallet.deriveFromMnemonic(mnemonic, 0);
            toastLongMessage(mnemonic);

        } catch (Exceptions.CannotDeriveKeysException cannotDeriveKeysException) {
            toastLongMessage("Error log into wallet!");
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_wallet_statistics, R.id.nav_bot_startegy, R.id.nav_others)
                .setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);

        walletName = headerView.findViewById(R.id.textView1);
        publicKey = headerView.findViewById(R.id.textView2);

        walletName.setText(wallet.toString());

        String hexCode = new String(Hex.encode(wallet.getPublicKey()));

        try{
            address = Address.fromHex(hexCode);
        }catch (DecoderException | Exceptions.AddressException decoderException){
            publicKey.setText(decoderException.toString());
        }

        String pubkey = "NULL";

        try {
            pubkey = address.bech32();
        }catch (Exceptions.AddressException addressException){
            addressException.printStackTrace();
        }

        publicKey.setText(pubkey);
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

    private void putMnemonicInMemmory(String key, String mnemonic) {
        editor = sharedPreferences.edit();
        editor.putString(key, mnemonic);
        editor.apply();
    }

    private void toastLongMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private String getStringFromByteArray(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
