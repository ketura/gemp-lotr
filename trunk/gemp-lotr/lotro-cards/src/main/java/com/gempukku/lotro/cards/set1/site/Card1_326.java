package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Type: Site
 * Site: 1
 * Game Text: Fellowship: Exert a Hobbit to play a companion or ally; that character's twilight cost is -1.
 */
public class Card1_326 extends AbstractSite {
    public Card1_326() {
        super("Westfarthing", "1_326", 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame lotroGame, PhysicalCard self) {
        // TODO Fellowship
        return null;
    }
}
