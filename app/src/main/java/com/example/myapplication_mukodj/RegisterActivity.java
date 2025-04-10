package com.example.myapplication_mukodj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    private static final int SECRET_KEY = 99;




    EditText UserNameEditText;
    EditText UserNameEmailEditText;
    EditText UserpasswordEditText;
    EditText UserpasswordagainEditText;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_acitvity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        int secret_key = getIntent().getIntExtra("SECRET_KEY",0);
        if(secret_key != 99){
            finish();
        }
        UserNameEmailEditText = findViewById(R.id.user_emailEditText);
        UserNameEditText= findViewById(R.id.user_nameEditText);
        UserpasswordEditText = findViewById(R.id.password_edittext);
        UserpasswordagainEditText = findViewById(R.id.password_again_edittext);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userNameEmail = preferences.getString("userNameEmail", "");
        String password = preferences.getString("password", "");

        UserNameEmailEditText.setText(userNameEmail);
        UserpasswordEditText.setText(password);
        UserpasswordagainEditText.setText(password);

        mAuth =  FirebaseAuth.getInstance();

        Log.i(LOG_TAG,"onCreate");


    }

    public void register(View view) {

        String userName = UserNameEditText.getText().toString();
        String userEmail = UserNameEmailEditText.getText().toString();
        String userPassword = UserpasswordEditText.getText().toString();
        String userPasswordagain = UserpasswordagainEditText.getText().toString();

        if(!userPassword.equals(userPasswordagain)){
            Log.e(LOG_TAG, "Hibás: Nem egyezik a jelszó és a megerősítése!" );
        }


        Log.i(LOG_TAG, "Regisztrált:"+userName+ " " + userEmail +" "+ userPassword);
        //startShopping();

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "User created successfully ");
                    startShopping();
                }else{
                    Log.d(LOG_TAG, "User wasn't created successfully");
                    Toast.makeText(RegisterActivity.this, "User wasn't created successfully: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    public void cancel(View view) {
        finish();
    }

    private void startShopping(/*register user data */){
        Intent intent = new Intent(this, Timetable_list_activity.class);
        //intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG,"onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG,"onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG,"onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG,"onRestart");
    }
}