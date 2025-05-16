package com.example.myapplication_mukodj;

public class Troli {
    private String szam;
    private String utirany;
    private String imgResource;

    // Üres konstruktor kell a Firestore‑nak
    public Troli() {}

    public Troli(String szam, String utirany, String imgResource) {
        this.szam = szam;
        this.utirany = utirany;
        this.imgResource = imgResource;
    }

    public String getSzam() {
        return szam;
    }

    public String getUtirany() {
        return utirany;
    }

    public String getImgResource() {
        return imgResource;
    }

    public void setSzam(String szam) {
        this.szam = szam;
    }

    public void setUtirany(String utirany) {
        this.utirany = utirany;
    }

    public void setImgResource(String imgResource) {
        this.imgResource = imgResource;
    }
}
