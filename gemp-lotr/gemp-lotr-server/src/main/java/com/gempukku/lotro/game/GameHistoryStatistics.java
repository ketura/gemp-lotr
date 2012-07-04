package com.gempukku.lotro.game;

import com.gempukku.lotro.db.GameHistoryDAO;

import java.util.*;

public class GameHistoryStatistics {
    private List<FormatStat> _formatStats;
    private long _start;
    private long _duration;

    public GameHistoryStatistics(long start, long duration) {
        _start = start;
        _duration = duration;
    }

    public void init(GameHistoryDAO gameHistoryDao) {
        Map<String, Integer> countsPerFormat = gameHistoryDao.getCasualGamesPlayedPerFormat(_start, _duration);
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (Map.Entry<String, Integer> formatCount : countsPerFormat.entrySet()) {
            String format = formatCount.getKey();
            if (!isIgnorable(format)) {
                incrementResult(result, getMainFormat(format), formatCount.getValue());
            }
        }

        int sum = 0;
        for (Integer integer : result.values())
            sum += integer;

        List<FormatStat> stats = new ArrayList<FormatStat>();
        for (Map.Entry<String, Integer> formatCount : result.entrySet())
            stats.add(new FormatStat(formatCount.getKey(), formatCount.getValue(), 1f * formatCount.getValue() / sum));

        Collections.sort(stats, new Comparator<FormatStat>() {
            @Override
            public int compare(FormatStat o1, FormatStat o2) {
                return o2.getCount() - o1.getCount();
            }
        });

        _formatStats = stats;
    }

    private void incrementResult(Map<String, Integer> result, String format, int value) {
        Integer previous = result.get(format);
        if (previous == null)
            previous = 0;
        result.put(format, previous + value);
    }

    private String getMainFormat(String format) {
        if (format.equals("Community Fellowship block"))
            return "Fellowship block";
        if (format.equals("Community Two Towers block"))
            return "Towers block";
        if (format.equals("Community War of the Ring block"))
            return "War of the Ring block";
        if (format.equals("Two Towers block"))
            return "Towers block";
        if (format.equals("Towers Standard"))
            return "Towers standard";
        return format;
    }

    private boolean isIgnorable(String format) {
        if (format.startsWith("Fellowship block - "))
            return true;
        if (format.equals("Format for testing"))
            return true;
        if (format.equals("Sealed FotR League (Dec 2011)"))
            return true;
        if (format.startsWith("Test league"))
            return true;
        if (format.startsWith("Towers block - "))
            return true;
        return false;
    }

    public List<FormatStat> getFormatStats() {
        return Collections.unmodifiableList(_formatStats);
    }

    public long getStart() {
        return _start;
    }

    public long getDuration() {
        return _duration;
    }

    public static class FormatStat {
        private String _format;
        private int _count;
        private float _percentage;

        public FormatStat(String format, int count, float percentage) {
            _format = format;
            _count = count;
            _percentage = percentage;
        }

        public int getCount() {
            return _count;
        }

        public String getFormat() {
            return _format;
        }

        public float getPercentage() {
            return _percentage;
        }
    }
}
