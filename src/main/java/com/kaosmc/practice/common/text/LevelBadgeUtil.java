package com.kaosmc.practice.common.text;

public final class LevelBadgeUtil {
    private LevelBadgeUtil() {
    }

    public static String getBadge(int experience) {
        int value = Math.max(0, experience);

        if (value <= 10) return format("&7", 10, "✺");
        if (value <= 20) return format("&a", 20, "❈");
        if (value <= 30) return format("&b", 30, "❄");
        if (value <= 40) return format("&e", 40, "✵");
        if (value <= 50) return format("&6", 50, "✶");
        if (value <= 60) return format("&5", 60, "✫");
        if (value <= 70) return format("&d", 70, "✿");
        if (value <= 80) return format("&c", 80, "❁");
        if (value <= 90) return format("&4", 90, "❂");
        if (value <= 100) return format("&f", 100, "✦");
        if (value <= 200) return format("&2", 200, "☘");
        if (value <= 300) return format("&3", 300, "❉");
        if (value <= 400) return format("&1", 400, "❊");
        if (value <= 500) return format("&e", 500, "✹");
        if (value <= 600) return format("&c", 600, "✸");
        if (value <= 700) return format("&d", 700, "✾");
        if (value <= 800) return format("&b", 800, "❅");
        if (value <= 900) return format("&6", 900, "✤");
        if (value <= 1000) return format("&4", 1000, "❂");

        int capped = ((value + 99) / 100) * 100;
        return format("&4", capped, "❂");
    }

    private static String format(String color, int number, String icon) {
        return CC.translate(color + "[" + number + icon + "]");
    }
}
