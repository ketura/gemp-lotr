package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * Relentless Armies
 * Fallen Realms	Condition • Support Area
 * Shadow: Remove (2) or discard this condition to heal an Easterling.
 */
public class Card20_134 extends AbstractPermanent {
    public Card20_134() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.FALLEN_REALMS, "Relentless Armies");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 2)
                || (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0) && PlayConditions.canSelfDiscard(self, game))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new RemoveTwilightEffect(2));
            possibleCosts.add(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Keyword.EASTERLING));
            return Collections.singletonList(action);
        }
        return null;
    }
}
