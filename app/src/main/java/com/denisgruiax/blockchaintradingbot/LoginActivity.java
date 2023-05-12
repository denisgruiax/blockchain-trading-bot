package com.denisgruiax.blockchaintradingbot;

import android.os.Bundle;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.denisgruiax.blockchaintradingbot.databinding.ActivityMainBinding;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(view -> {

            List<String> test = new ArrayList<>();
            test.add("drill");

            Toast toast = Toast.makeText(getApplicationContext(), "result = " + checkAllFields(test), Toast.LENGTH_LONG);
            toast.show();
        });
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //textView.setText(savedInstanceState.getString("Denis"));
    }

    // Invoked when the activity might be temporarily destroyed; save the instance state here.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("public", "denis");
        outState.putString("private", "pass");

        // Call superclass to save any view hierarchy.
        super.onSaveInstanceState(outState);
    }

    private boolean checkWord(String word) {
        String line;
        InputStream inputStream = getResources().openRawResource(R.raw.bip39_words);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = bufferedReader.readLine()) != null)
            {
                System.out.println(line + " - " + word);
                if (line.contains(word))
                    return true;
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return false;
    }

    private boolean checkAllFields(List<String> words) {
        for(String word : words)
            if(!checkWord(word))
                return false;

        return true;
    }
}