package hu.pizzapp;

import com.google.gson.annotations.SerializedName;

public class Pizza {
    private int id;
    private String nev;
    private String kategorianev;
    private boolean vegetarianus;

    @SerializedName("image_url")
    private String image_url;

    // getterek
    public int getId() { return id; }
    public String getNev() { return nev; }
    public String getKategorianev() { return kategorianev; }
    public boolean isVegetarianus() { return vegetarianus; }
    public String getImageUrl() { return image_url; }
}
