package com.gempukku.lotro.cards.build.field.effect.appender.resolver;

import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.common.Phase;

public class TimeResolver {
    public static Time resolveTime(Object value, String defaultValue) throws InvalidCardDefinitionException {
        if (value == null)
            return parseTime(defaultValue.toLowerCase());
        if (value instanceof String)
            return parseTime(((String) value).toLowerCase());

        throw new InvalidCardDefinitionException("Unable to resolve time: " + value);
    }

    private static Time parseTime(String value) throws InvalidCardDefinitionException {
        if (value.startsWith("start(") && value.endsWith(")")) {
            final String phaseName = value.substring(value.indexOf("(") + 1, value.lastIndexOf(")"));
            return new Time(Phase.valueOf(phaseName.toUpperCase()), true, false);
        }
        else if (value.startsWith("end(") && value.endsWith(")")) {
            final String phaseName = value.substring(value.indexOf("(") + 1, value.lastIndexOf(")"));
            if (phaseName.equals("current"))
                return new Time(null, false, false);
            return new Time(Phase.valueOf(phaseName.toUpperCase()), false, false);
        }
        else if (value.equals("endofturn"))
            return new Time(null, false, true);

        throw new InvalidCardDefinitionException("Unable to resolve time: " + value);
    }

    public static class Time {
        private Phase phase;
        private boolean start;
        private boolean endOfTurn;

        private Time(Phase phase, boolean start, boolean endOfTurn) {
            this.phase = phase;
            this.start = start;
            this.endOfTurn = endOfTurn;
        }

        public Phase getPhase() {
            return phase;
        }

        public boolean isStart() {
            return start;
        }

        public boolean isEndOfTurn() {
            return endOfTurn;
        }

        public String getHumanReadable() {
            if (endOfTurn) {
                return "the end of the turn";
            }
            else if (phase == null) {
                return "the end of the current phase";
            }
            else if (start) {
                return "the start of the " + phase.getHumanReadable() + " phase";
            }
            else {
                return "the end of the " + phase.getHumanReadable() + " phase";
            }
        }
    }
}
