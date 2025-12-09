package hu.pizzapp;

public class Pizza {
    private int id;
    private String nev;
    private String kategorianev;
    private boolean vegetarianus;
    private String image;

    // getterek
    public int getId() { return id; }
    public String getNev() { return nev; }
    public String getKategorianev() { return kategorianev; }
    public boolean isVegetarianus() { return vegetarianus; }
    public String getImage() { return image; }
}