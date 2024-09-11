package me.t3sl4.ondergrup.Model.Country;

public class Country {
    private int flagResId;
    private String name;

    public Country(int flagResId, String name) {
        this.flagResId = flagResId;
        this.name = name;
    }

    public int getFlagResId() {
        return flagResId;
    }

    public String getName() {
        return name;
    }
}