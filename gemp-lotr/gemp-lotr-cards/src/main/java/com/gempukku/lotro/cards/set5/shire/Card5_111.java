package com.gempukku.lotro.cards.set5.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Signet: Aragorn
 * Game Text: Ring-bearer (resistance 10). Fellowship: Add a burden to play Smeagol from your discard pile.
 */
public class Card5_111 extends AbstractCompanion {
    public Card5_111() {
        super(0, 3, 4, 10, Culture.SHIRE, Race.HOBBIT, Signet.ARAGORN, "Frodo", "Master of the Precious", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, Filters.smeagol)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddBurdenEffect(self.getOwner(), self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.smeagol));
            return Collections.singletonList(action);
        }
        return null;
    }
}
