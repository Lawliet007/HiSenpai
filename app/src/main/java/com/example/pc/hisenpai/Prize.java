package com.example.pc.hisenpai;

public class Prize {
    private int tier;
    private String name;

    public Prize(int tier, String name) {
        this.tier = tier;
        this.name = name;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
