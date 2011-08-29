package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion � Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Signet: Frodo
 * Game Text: Ring-bearer (resistance 10). Fellowship: Exert another companion who has the Frodo signet to heal Frodo.
 */
public class Card1_290 extends AbstractCompanion {
    public Card1_290() {
        super(0, 3, 4, Culture.SHIRE, "Frodo", "1_290", true);
        setSignet(Signet.FRODO);
        addKeyword(Keyword.HOBBIT);
        addKeyword(Keyword.RING_BEARER);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame lotroGame, PhysicalCard self) {
        LinkedList<Action> result = new LinkedList<Action>();

        appendPlayCompanionActions(result, lotroGame, self);
        appendHealCompanionActions(result, lotroGame, self);

        // TODO Fellowship action

        return result;
    }
}
