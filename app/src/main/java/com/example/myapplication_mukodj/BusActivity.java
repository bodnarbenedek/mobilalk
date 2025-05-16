package com.example.myapplication_mukodj;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BusActivity extends AppCompatActivity {

    private Spinner spinnerLine, spinnerDirection;
    private Button btnShowDepartures;
    private ImageView imgSchedule;

    private FirebaseFirestore mFirestore;
    private List<Bus> busList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busz);

        spinnerLine       = findViewById(R.id.spinnerLine);
        spinnerDirection  = findViewById(R.id.spinnerDirection);
        btnShowDepartures = findViewById(R.id.btnShowDepartures);
        imgSchedule       = findViewById(R.id.itemImage);

        mFirestore = FirebaseFirestore.getInstance();

        // 1) Lekérjük az összes bus dokumentumot a "buses" kollekcióból
        mFirestore.collection("buses")
                .get()
                .addOnSuccessListener(qSnap -> {
                    busList.clear();
                    for (var doc : qSnap.getDocuments()) {
                        Bus b = doc.toObject(Bus.class);
                        if (b != null) busList.add(b);
                    }

                    // Kinyerjük az egyedi vonalakat
                    Set<String> lines = new TreeSet<>();
                    for (Bus b : busList) {
                        lines.add(b.getSzam());
                    }

                    ArrayAdapter<String> lineAdapter = new ArrayAdapter<>(
                            BusActivity.this,
                            android.R.layout.simple_spinner_item,
                            new ArrayList<>(lines)
                    );
                    lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLine.setAdapter(lineAdapter);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Hiba a buszok lekérdezésében: " + e.getMessage(),
                                Toast.LENGTH_LONG).show()
                );

        // 2) Line-spinner választásakor töltsük be az irányokat
        spinnerLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedLine = parent.getItemAtPosition(pos).toString();
                Set<String> directions = new LinkedHashSet<>();
                for (Bus b : busList) {
                    if (b.getSzam().equals(selectedLine)) {
                        directions.add(b.getUtirany());
                    }
                }
                ArrayAdapter<String> dirAdapter = new ArrayAdapter<>(
                        BusActivity.this,
                        android.R.layout.simple_spinner_item,
                        new ArrayList<>(directions)
                );
                dirAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDirection.setAdapter(dirAdapter);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 3) Gombnyomásra jelenítsük meg az adott vonal+irány képét
        btnShowDepartures.setOnClickListener(v -> {
            String line = (String) spinnerLine.getSelectedItem();
            String dir  = (String) spinnerDirection.getSelectedItem();
            for (Bus b : busList) {
                if (b.getSzam().equals(line) && b.getUtirany().equals(dir)) {
                    int resId = getResources().getIdentifier(
                            b.getImgResource(), "drawable", getPackageName()
                    );
                    if (resId != 0) {
                        imgSchedule.setImageResource(resId);
                    } else {
                        imgSchedule.setImageDrawable(null);
                        Toast.makeText(this,
                                "Kép nem található: " + b.getImgResource(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                    return;
                }
            }
            Toast.makeText(this,
                    "Nincs menetrend ehhez a választáshoz.",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}
