package com.gempukku.lotro.cards.set9.gollum;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.PutCardsFromHandBeneathDrawDeckEffect;
import com.gempukku.lotro.logic.effects.RevealCardsFromYourHandEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Reflections
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While Gollum is at a mountain, river, or underground site, he is strength +2. Regroup: Spot Gollum to
 * reveal your hand. Place all Shadow cards revealed beneath your draw deck. Discard this condition.
 */
public class Card9_029 extends AbstractPermanent {
    public Card9_029() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.GOLLUM, "Slippery as Fishes");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.gollum, new LocationCondition(Filters.or(Keyword.MOUNTAIN, Keyword.RIVER, Keyword.UNDERGROUND)), 2));
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canSpot(game, Filters.gollum)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new RevealCardsFromYourHandEffect(self, playerId, new LinkedList<PhysicalCard>(game.getGameState().getHand(playerId))));
            action.appendEffect(
                    new PutCardsFromHandBeneathDrawDeckEffect(action, playerId, Side.SHADOW));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
