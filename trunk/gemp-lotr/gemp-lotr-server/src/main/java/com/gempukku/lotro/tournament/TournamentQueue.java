package com.gempukku.lotro.tournament;

import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.logic.vo.LotroDeck;

public interface TournamentQueue {
    public LotroFormat getLotroFormat();

    public CollectionType getCollectionType();

    public String getTournamentQueueName();

    public boolean process(TournamentQueueCallback tournamentQueueCallback);

    public void joinPlayer(String player, LotroDeck deck);

    public void leavePlayer(String player);

    public int getPlayerCount();

    public boolean isPlayerSignedUp(String player);
}
