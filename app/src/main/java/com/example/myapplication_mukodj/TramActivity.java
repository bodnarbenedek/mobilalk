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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TramActivity extends AppCompatActivity {

    private Spinner spinnerLine, spinnerDirection;
    private Button btnShowDepartures;
    private ImageView imgSchedule;

    private FirebaseFirestore mFirestore;

    // A lekért villamosok ide kerülnek
    private List<Tram> tramList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.villamos);

        spinnerLine       = findViewById(R.id.spinnerLine);
        spinnerDirection  = findViewById(R.id.spinnerDirection);
        btnShowDepartures = findViewById(R.id.btnShowDepartures);
        imgSchedule       = findViewById(R.id.itemImage);

        mFirestore = FirebaseFirestore.getInstance();

        // Lekérjük az összes tram dokumentumot
        mFirestore.collection("trams").orderBy("szam")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    tramList.clear();
                    for (var doc : queryDocumentSnapshots.getDocuments()) {
                        Tram t = doc.toObject(Tram.class);
                        if (t != null) tramList.add(t);
                    }

                    // Kinyerjük az elérhető vonalakat (duplikáció nélkül)
                    Set<String> lines = new TreeSet<>();
                    for (Tram t : tramList) {
                        lines.add(t.getSzam());
                    }

                    ArrayAdapter<String> lineAdapter = new ArrayAdapter<>(
                            TramActivity.this,
                            android.R.layout.simple_spinner_item,
                            new ArrayList<>(lines)
                    );
                    lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLine.setAdapter(lineAdapter);
                });

        // Amikor vonalat választanak:
        spinnerLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedLine = parent.getItemAtPosition(pos).toString();

                // Irányok kigyűjtése ehhez a vonalhoz
                Set<String> directions = new LinkedHashSet<>();
                for (Tram t : tramList) {
                    if (t.getSzam().equals(selectedLine)) {
                        directions.add(t.getUtirany());
                    }
                }

                ArrayAdapter<String> dirAdapter = new ArrayAdapter<>(
                        TramActivity.this,
                        android.R.layout.simple_spinner_item,
                        new ArrayList<>(directions)
                );
                dirAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDirection.setAdapter(dirAdapter);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Amikor megnyomják a menetrend gombot:
        btnShowDepartures.setOnClickListener(v -> {
            String line = (String) spinnerLine.getSelectedItem();
            String direction = (String) spinnerDirection.getSelectedItem();

            // Megkeressük a megfelelő Tram objektumot
            for (Tram t : tramList) {
                if (t.getSzam().equals(line) && t.getUtirany().equals(direction)) {
                    // image resource név alapján betöltjük a képet
                    int resId = getResources().getIdentifier(t.getImgResource(), "drawable", getPackageName());
                    if (resId != 0) {
                        imgSchedule.setImageResource(resId);
                    } else {
                        imgSchedule.setImageDrawable(null);
                        Toast.makeText(this, "Nem található kép: " + t.getImgResource(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }

            Toast.makeText(this, "Nincs menetrend ehhez a választáshoz.", Toast.LENGTH_SHORT).show();
        });
    }
}
