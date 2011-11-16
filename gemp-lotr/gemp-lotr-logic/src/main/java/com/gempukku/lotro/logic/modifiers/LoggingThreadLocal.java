package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import org.apache.log4j.Logger;

public class LoggingThreadLocal {
    private static final Logger LOG = Logger.getLogger(LoggingThreadLocal.class);
    private static ThreadLocal<StringBuilder> _logLocal = new ThreadLocal<StringBuilder>();
    private static ThreadLocal<Long> _start = new ThreadLocal<Long>();

    public static void start() {
        _logLocal.set(new StringBuilder());
        _start.set(System.currentTimeMillis());
    }

    public static void log(PhysicalCard card, String desc) {
        StringBuilder builder = _logLocal.get();
        if (builder != null && card != null)
            builder.append(card.getBlueprintId()).append(" ").append(desc).append(" - ");
    }

    public static void stop(boolean writeOperation) {
        long start = _start.get();
        long time = System.currentTimeMillis() - start;
        if (time > 100)
            LOG.error("Processing took (" + writeOperation + ")" + time + "ms: \n" + _logLocal.get().toString());
        _start.set(null);
        _logLocal.set(null);
    }
}
