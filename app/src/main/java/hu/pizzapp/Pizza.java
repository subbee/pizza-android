package hu.pizzapp;

import com.google.gson.annotations.SerializedName;

public class Pizza {
    private int id;
    private String nev;
    private String kategorianev;
    private boolean vegetarianus;
    private String ar;

    @SerializedName("image_url")
    private String image_url;

    // getterek
    public int getId() { return id; }
    public String getNev() { return nev; }
    public String getKategorianev() { return kategorianev; }
    public String getAr() { return ar + " Ft"; }
    public String getVegetarianus() { return vegetarianus ? "VEGA" : ""; }
    public String getImageUrl() { return image_url; }
}
