package com.gempukku.lotro.cards.build.field.effect.appender.resolver;

import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;

public class CountResolver {
    public static Count resolveCount(Object value, int defaultValue) throws InvalidCardDefinitionException {
        if (value == null)
            return new Count(defaultValue, defaultValue);

        try {
            if (value instanceof String) {
                String stringValue = (String) value;
                if (stringValue.contains("-")) {
                    final String[] split = stringValue.split("-", 2);
                    final int min = Integer.parseInt(split[0]);
                    final int max = Integer.parseInt(split[1]);
                    if (min > max || min < 0 || max < 1)
                        throw new InvalidCardDefinitionException("Unable to resolve count: " + value);
                    return new Count(min, max);
                } else {
                    int v = Integer.parseInt(stringValue);
                    return new Count(v, v);
                }
            } else if (value instanceof Number) {
                return new Count(((Number) value).intValue(), ((Number) value).intValue());
            }
        } catch (NumberFormatException exp) {
            throw new InvalidCardDefinitionException("Unable to resolve count: " + value, exp);
        }
        throw new InvalidCardDefinitionException("Unable to resolve count: " + value);
    }

    public static class Count {
        private int min;
        private int max;

        private Count(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }
    }
}
