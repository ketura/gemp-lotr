package com.gempukku.lotro.tournament;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.logic.vo.LotroDeck;

public interface TournamentQueue {
    public int getCost();

    public String getFormat();

    public CollectionType getCollectionType();

    public String getTournamentQueueName();

    public boolean isRequiresDeck();

    public boolean process(TournamentQueueCallback tournamentQueueCallback);

    public void joinPlayer(CollectionsManager collectionsManager, Player player, LotroDeck deck);

    public void leavePlayer(CollectionsManager collectionsManager, Player player);

    public void leaveAllPlayers(CollectionsManager collectionsManager);

    public int getPlayerCount();

    public boolean isPlayerSignedUp(String player);
}
