package nazenov.functions.Champions.findChamp;

import static nazenov.utils.ChampionsNames.championNames;

public class SearchName {
    public String findBestMatch(String championName) {
        String bestMatch = null;
        int bestDistance = Integer.MAX_VALUE;

        for (String name : championNames) {
            int distance = calculateLevenshteinDistance( championName.toLowerCase(), name.toLowerCase() );

            if (distance < bestDistance) {
                bestMatch = name;
                bestDistance = distance;
            }
        }

        int threshold = 3;

        if (bestDistance <= threshold) {
            return bestMatch;
        } else {
            return null;
        }
    }

    // Custom Levenshtein distance calculation
    public static int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt( i - 1 ) != s2.charAt( j - 1 )) ? 1 : 0;
                    dp[i][j] = Math.min( Math.min( dp[i - 1][j] + 1, dp[i][j - 1] + 1 ), dp[i - 1][j - 1] + cost );
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }
}
