package com.example.myapplication_mukodj;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

public class DataSeeder {
    private static final String TAG = "DataSeeder";

    public static void seedAll(Context context) {
        seedTrams(context);
        seedBuses(context);
        seedTrolis(context);
    }

    public static void seedTrams(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();
        Resources res = context.getResources();
        String pkg = context.getPackageName();

        String[] lines = res.getStringArray(R.array.spinner_lines_array_tram);
        for (String line : lines) {
            String dirArray = "spinner_directions_tram_line" + line.toLowerCase();
            int dirRes = res.getIdentifier(dirArray, "array", pkg);
            String imgArray = "tram_line" + line.toLowerCase() + "_images";
            int imgRes = res.getIdentifier(imgArray, "array", pkg);
            if (dirRes == 0 || imgRes == 0) {
                Log.w(TAG, "Missing tram arrays for line " + line);
                continue;
            }
            String[] directions = res.getStringArray(dirRes);
            TypedArray images = res.obtainTypedArray(imgRes);
            for (int i = 0; i < directions.length; i++) {
                String ut = directions[i];
                int drId = images.getResourceId(i, 0);
                if (drId == 0) continue;
                String imgName = res.getResourceEntryName(drId);
                Tram t = new Tram(line, ut, imgName);
                String docId = "tram_" + line + "_" + imgName;
                DocumentReference ref = db.collection("trams").document(docId);
                batch.set(ref, t);
            }
            images.recycle();
        }

        batch.commit()
                .addOnSuccessListener(a-> Log.i(TAG, "seedTrams ok"))
                .addOnFailureListener(e-> Log.e(TAG, "seedTrams err", e));
    }

    public static void seedBuses(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();
        Resources res = context.getResources();
        String pkg = context.getPackageName();

        String[] lines = res.getStringArray(R.array.spinner_lines_array_bus);
        for (String line : lines) {
            String dirArray = "spinner_directions_array_bus_line" + line;
            int dirRes = res.getIdentifier(dirArray, "array", pkg);
            String imgArray = "busz_line" + line + "_images";
            int imgRes = res.getIdentifier(imgArray, "array", pkg);
            if (dirRes == 0 || imgRes == 0) {
                Log.w(TAG, "Missing bus arrays for line " + line);
                continue;
            }
            String[] directions = res.getStringArray(dirRes);
            TypedArray images = res.obtainTypedArray(imgRes);
            for (int i = 0; i < directions.length; i++) {
                String ut = directions[i];
                int drId = images.getResourceId(i, 0);
                if (drId == 0) continue;
                String imgName = res.getResourceEntryName(drId);
                Bus b = new Bus(line, ut, imgName);
                String docId = "bus_" + line + "_" + imgName;
                DocumentReference ref = db.collection("buses").document(docId);
                batch.set(ref, b);
            }
            images.recycle();
        }

        batch.commit()
                .addOnSuccessListener(a-> Log.i(TAG, "seedBuses ok"))
                .addOnFailureListener(e-> Log.e(TAG, "seedBuses err", e));
    }

    public static void seedTrolis(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();
        Resources res = context.getResources();
        String pkg = context.getPackageName();

        String[] lines = res.getStringArray(R.array.spinner_lines_array_troli);
        for (String line : lines) {
            String dirArray = "spinner_directions_array_troli_line" + line.toLowerCase();
            int dirRes = res.getIdentifier(dirArray, "array", pkg);
            String imgArray = "troli_line" + line.toLowerCase() + "_images";
            int imgRes = res.getIdentifier(imgArray, "array", pkg);
            if (dirRes == 0 || imgRes == 0) {
                Log.w(TAG, "Missing troli arrays for line " + line);
                continue;
            }
            String[] directions = res.getStringArray(dirRes);
            TypedArray images = res.obtainTypedArray(imgRes);
            for (int i = 0; i < directions.length; i++) {
                String ut = directions[i];
                int drId = images.getResourceId(i, 0);
                if (drId == 0) continue;
                String imgName = res.getResourceEntryName(drId);
                // Troli száma string, pl. "1E"
                Troli t = new Troli(line, ut, imgName);
                String docId = "troli_" + line + "_" + imgName;
                DocumentReference ref = db.collection("trolis").document(docId);
                batch.set(ref, t);
            }
            images.recycle();
        }

        batch.commit()
                .addOnSuccessListener(a-> Log.i(TAG, "seedTrolis ok"))
                .addOnFailureListener(e-> Log.e(TAG, "seedTrolis err", e));
    }
}
