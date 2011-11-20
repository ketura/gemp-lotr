package com.gempukku.lotro.cards.set9.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PutCardsFromHandBeneathDrawDeckEffect;
import com.gempukku.lotro.cards.effects.RevealCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

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
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "Slippery as Fishes");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.gollum, new LocationCondition(Filters.or(Keyword.MOUNTAIN, Keyword.RIVER, Keyword.UNDERGROUND)), 2));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canSpot(game, Filters.gollum)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new RevealCardsFromHandEffect(self, playerId, new LinkedList<PhysicalCard>(game.getGameState().getHand(playerId))));
            action.appendEffect(
                    new PutCardsFromHandBeneathDrawDeckEffect(action, playerId, Side.SHADOW));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
