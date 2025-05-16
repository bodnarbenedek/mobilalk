package com.example.myapplication_mukodj;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDataActivity extends AppCompatActivity {

    private TextView tvUsernameValue;
    private TextView tvEmailValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        tvUsernameValue = findViewById(R.id.tvUsernameValue);
        tvEmailValue    = findViewById(R.id.tvEmailValue);

        // 1) Lekérjük a bejelentkezett felhasználót
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Ha mégis vendég vagy kijelentkezett állapotban ér ide
            Toast.makeText(this, "Nincs bejelentkezve felhasználó.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2) Beállítjuk a megjelenítendő nevet és emailt
        String displayName = user.getDisplayName();
        if (displayName == null || displayName.isEmpty()) {
            displayName = "(Nincs beállítva név)";
        }
        tvUsernameValue.setText(displayName);
        tvEmailValue.setText(user.getEmail());

        findViewById(R.id.btnEditUser).setOnClickListener(v -> {
            // Átirányítunk az EditUserActivity-re
            startActivity(new Intent(UserDataActivity.this, EditUserActivity.class));
        });

        // 3) Törlés gomb
        findViewById(R.id.btnDeleteUser).setOnClickListener(v -> {
            new AlertDialog.Builder(UserDataActivity.this)
                    .setTitle("Biztosan törölni szeretnéd a fiókodat?")
                    .setMessage("Ez a művelet végleges, később nem vonható vissza.")
                    .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUser();
                        }
                    })
                    .setNegativeButton("Mégse", null)
                    .show();
        });
    }

    private void deleteUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Nincs bejelentkezve felhasználó.", Toast.LENGTH_SHORT).show();
            return;
        }

        user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UserDataActivity.this,
                        "Felhasználói fiók sikeresen törölve.",
                        Toast.LENGTH_SHORT).show();
                // Vissza a bejelentkező képernyőre (pl. LoginActivity)
                Intent intent = new Intent(UserDataActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(UserDataActivity.this,
                        "Fiók törlése sikertelen: " + task.getException().getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUserData(); // Frissítjük a felhasználói adatokat minden alkalommal, amikor visszatérünk ide
    }

    private void refreshUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Nincs bejelentkezve felhasználó.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String displayName = user.getDisplayName();
        if (displayName == null || displayName.isEmpty()) {
            displayName = "(Nincs beállítva név)";
        }

        tvUsernameValue.setText(displayName);
        tvEmailValue.setText(user.getEmail());
    }
}
