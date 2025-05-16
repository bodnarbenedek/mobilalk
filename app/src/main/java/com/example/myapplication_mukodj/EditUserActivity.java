package com.example.myapplication_mukodj;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditUserActivity extends AppCompatActivity {

    private EditText etName, etEmail;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        etName  = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSaveUser);

        // Betöltjük a jelenlegi értékeket
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String displayName = user.getDisplayName();
            if (!TextUtils.isEmpty(displayName)) {
                etName.setText(displayName);
            }
            etEmail.setText(user.getEmail());
        }

        btnSave.setOnClickListener(v -> {
            String newName  = etName.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newEmail)) {
                Toast.makeText(this, "Kérlek töltsd ki mindkét mezőt.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Frissítjük először a profilt (név)
            if (user != null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newName)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(profileTask -> {
                            if (!profileTask.isSuccessful()) {
                                Toast.makeText(this, "Név frissítése sikertelen.", Toast.LENGTH_SHORT).show();
                            }
                            // Ezután frissítjük az emailt
                            user.updateEmail(newEmail)
                                    .addOnCompleteListener(emailTask -> {
                                        if (emailTask.isSuccessful()) {
                                            Toast.makeText(this, "Adatok mentve.", Toast.LENGTH_SHORT).show();
                                            finish();  // vissza az előző képernyőre
                                        } else {
                                            Toast.makeText(this,
                                                    "Email frissítése sikertelen: " + emailTask.getException().getMessage(),
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    });
                        });
            }
        });
    }
}

