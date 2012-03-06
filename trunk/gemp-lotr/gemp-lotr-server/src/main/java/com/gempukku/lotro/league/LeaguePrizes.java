package com.gempukku.lotro.league;

public class LeaguePrizes {
    public static void main(String[] args) {
        int players = 32;
        int lastDisplayed = 0;
        int lastCount = Integer.MAX_VALUE;
        for (int i = 1; i <= players; i++) {
            int count = (int) Math.floor((2 * players + 24) / (i + 9) - 2.4);
            if (lastCount != count) {
                if (lastDisplayed > 0 && lastCount > 0) {
                    if (lastDisplayed == (i - 1))
                        System.out.println(lastDisplayed + ": " + lastCount);
                    else
                        System.out.println(lastDisplayed + "-" + (i - 1) + ": " + lastCount);
                }
                lastCount = count;
                lastDisplayed = i;
            }
        }
    }
}
