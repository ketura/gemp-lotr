package com.gempukku.lotro.cards.set12.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Skirmish: Discard this condition to heal a [GONDOR] Man (or to heal 2 [GONDOR] Men if you can spot
 * a roaming minion).
 */
public class Card12_051 extends AbstractPermanent {
    public Card12_051() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, "Invigorated");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            int healCount = PlayConditions.canSpot(game, Filters.roamingMinion) ? 2 : 1;
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, healCount, healCount, Culture.GONDOR, Race.MAN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
