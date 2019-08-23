package com.gempukku.lotro.cards.set15.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.ReinforceTokenEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Ent
 * Strength: 8
 * Vitality: 3
 * Resistance: 6
 * Game Text: Quickbeam is twilight cost -1 for each Ent you can spot. Maneuver: Discard this companion to place another
 * [GANDALF] companion from your discard pile on the bottom of your draw deck and reinforce a [GANDALF] token.
 */
public class Card15_034 extends AbstractCompanion {
    public Card15_034() {
        super(4, 8, 3, 6, Culture.GANDALF, Race.ENT, null, "Quickbeam", "Hastiest of All Ents", true);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return -Filters.countActive(game, Race.ENT)
                - game.getModifiersQuerying().getSpotBonus(game, Race.ENT);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardOnBottomOfDeckEffect(action, playerId, 1, 1, Culture.GANDALF, CardType.COMPANION, Filters.not(self)));
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.GANDALF));
            return Collections.singletonList(action);
        }
        return null;
    }
}
