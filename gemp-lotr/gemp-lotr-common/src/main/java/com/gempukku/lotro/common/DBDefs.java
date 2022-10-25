package com.gempukku.lotro.common;

import java.time.LocalDateTime;
import java.util.Date;

public class DBDefs {

    public static class GameHistory {

        public int id;

        public String winner;
        public String loser;

        public String win_reason;
        public String lose_reason;

        public String win_recording_id;
        public String lose_recording_id;

        public long start_date;
        public long end_date;

        public String format_name;

        public String winner_deck_name;
        public String loser_deck_name;

        public String tournament;


        public Date GetStartDate()
        {
            return new Date(start_date);
        }

        public Date GetEndDate()
        {
            return new Date(end_date);
        }


    }

    public static class CollectionEntry {
        public int collection_id;
        public int quantity;
        public String product_type;
        public String product_variant;
        public String product;
        public String source;
        public LocalDateTime created_date;
        public LocalDateTime modified_date;
        public String notes;
    }
}
