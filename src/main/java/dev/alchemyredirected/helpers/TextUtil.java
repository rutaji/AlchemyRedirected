package dev.alchemyredirected.helpers;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;

public class TextUtil {
    public static String toRomanNumeral(int i) {
        if (i <= 1) { // level I is not written by vanilla effects
            return "";
        }
        StringBuilder result = new StringBuilder();
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        for (int n = 0; n < values.length; n++) {
            while (i >= values[n]) {
                result.append(symbols[n]);
                i -= values[n];
            }
        }
        return result.toString();
    }
    public static String ticksToTime(int ticks) {
        if(ticks == 0){return "0";}
        int totalSeconds = ticks / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static TextColor colorToTextColor(Color color) {
        return TextColor.color(color.asRGB());
    }
}
