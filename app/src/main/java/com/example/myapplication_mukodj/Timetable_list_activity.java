package com.example.myapplication_mukodj;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Timetable_list_activity extends AppCompatActivity {

    private static final String LOG_TAG = Timetable_list_activity.class.getName();
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_SEEDED = "data_seeded";
    private FirebaseUser user;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // window tranzíciók engedélyezése
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());

        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean seeded = prefs.getBoolean(KEY_SEEDED, false);
        if (!seeded) {
            DataSeeder.seedAll(this);
            prefs.edit()
                    .putBoolean(KEY_SEEDED, true)
                    .apply();
            Log.i(LOG_TAG, "Adatok első indításkor feltöltve (seedAll).");
        }


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timetable_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
            return;
        }
        Log.d(LOG_TAG, "Authenticated user!");

        // View-k
        ImageView tramImage = findViewById(R.id.villamos);
        TextView  tramText  = findViewById(R.id.tvVillamosTitle);
        ImageView busImage  = findViewById(R.id.busz);
        TextView  busText   = findViewById(R.id.tvBuszTitle);
        ImageView troliImage= findViewById(R.id.troli);
        TextView  troliText = findViewById(R.id.tvTroliTitle);

        // Fade-in animáció
        tramImage.setAlpha(0f);
        busImage.setAlpha(0f);
        troliImage.setAlpha(0f);
        tramImage.animate().alpha(1f).setDuration(1000).start();
        busImage.animate() .alpha(1f).setDuration(1000).setStartDelay(300).start();
        troliImage.animate().alpha(1f).setDuration(1000).setStartDelay(600).start();

        // Kattintás: shared-element átmenettel indítjuk az Activity-ket
        View.OnClickListener openTram = v -> {
            Intent intent = new Intent(this, TramActivity.class);
            ActivityOptions opts = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    tramImage,
                    ViewCompat.getTransitionName(tramImage)  // "vili"
            );
            startActivity(intent, opts.toBundle());
        };
        tramImage.setOnClickListener(openTram);
        tramText .setOnClickListener(openTram);

        View.OnClickListener openBus = v -> {
            Intent intent = new Intent(this, BusActivity.class);
            ActivityOptions opts = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    busImage,
                    ViewCompat.getTransitionName(busImage)  // "bus"
            );
            startActivity(intent, opts.toBundle());
        };
        busImage.setOnClickListener(openBus);
        busText .setOnClickListener(openBus);

        View.OnClickListener openTroli = v -> {
            Intent intent = new Intent(this, TroliActivity.class);
            ActivityOptions opts = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    troliImage,
                    ViewCompat.getTransitionName(troliImage)  // "trol"
            );
            startActivity(intent, opts.toBundle());
        };
        troliImage.setOnClickListener(openTroli);
        troliText .setOnClickListener(openTroli);
    }

    // Lifecycle naplók...
    @Override protected void onStart()   { super.onStart();   Log.i(LOG_TAG, "onStart"); }
    @Override protected void onResume()  { super.onResume(); }
    @Override protected void onPause()   { super.onPause(); }
    @Override protected void onStop()    { super.onStop(); }
    @Override protected void onRestart() { super.onRestart(); }
    @Override protected void onDestroy() { super.onDestroy(); }

    public void user(View view) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && !currentUser.isAnonymous()) {
            // Be van jelentkezve nem vendégként
            Intent intent = new Intent(this, UserDataActivity.class);
            startActivity(intent);
        } else {
            // Vendégként van belépve (vagy nem is jelentkezett be)
            Toast.makeText(
                    this,
                    "Vendégként van belépve.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
