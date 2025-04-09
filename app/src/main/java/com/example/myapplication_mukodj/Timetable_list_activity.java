package com.example.myapplication_mukodj;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutXmlParser;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Timetable_list_activity extends AppCompatActivity {

    private static final String LOG_TAG = Timetable_list_activity.class.getName();
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timetable_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(LOG_TAG, "Authenticated user!");
        }else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        // Lekérjük a képeket
        final View villamos = findViewById(R.id.villamos);
        final View busz = findViewById(R.id.busz);
        final View troli = findViewById(R.id.troli);

        // Kezdetben átlátszóvá tesszük
        villamos.setAlpha(0f);
        busz.setAlpha(0f);
        troli.setAlpha(0f);

        // Animáció elindítása
        villamos.animate().alpha(1f).setDuration(1000).start(); // 1 másodperc fade-in
        busz.animate().alpha(1f).setDuration(1000).setStartDelay(300).start(); // kis késleltetés
        troli.animate().alpha(1f).setDuration(1000).setStartDelay(600).start(); // még több késleltetés


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG,"onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}