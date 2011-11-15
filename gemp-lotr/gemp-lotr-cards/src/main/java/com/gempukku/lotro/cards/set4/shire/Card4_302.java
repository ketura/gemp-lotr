package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Signet: Gandalf
 * Game Text: Ring-bearer (resistance 10). Fellowship: Play a Ring-bound companion to remove a burden.
 */
public class Card4_302 extends AbstractCompanion {
    public Card4_302() {
        super(0, 3, 4, Culture.SHIRE, Race.HOBBIT, Signet.GANDALF, "Frodo", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public int getResistance() {
        return 10;
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromHand(playerId, game, CardType.COMPANION, Keyword.RING_BOUND)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), CardType.COMPANION, Keyword.RING_BOUND));
            action.appendEffect(
                    new RemoveBurdenEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
