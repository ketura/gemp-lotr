package com.gempukku.lotro.draft;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.logic.vo.LotroDeck;

public interface Draft {
    public void advanceDraft(DraftCallback draftCallback);

    public void playerChosenCard(String playerName, String cardId);

    public void playerSummittedDeck(Player player, LotroDeck deck) throws DeckInvalidException;

    public CardCollection getCardChoice(String playerName);
}
