package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. While the fellowship is at site 6T, each Ring-bound Man takes no more than
 * 1 wound during each skirmish phase. Fellowship: Add (2) and discard this condition to heal a Ring-bound Man.
 */
public class Card4_120 extends AbstractPermanent {
    public Card4_120() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Forbidden Pool", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WOUND
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && game.getGameState().getCurrentSiteNumber() == 6
                && game.getGameState().getCurrentSiteBlock() == Block.TWO_TOWERS) {
            WoundResult woundResult = (WoundResult) effectResult;
            Collection<PhysicalCard> woundedRingBounds = Filters.filter(woundResult.getWoundedCards(), game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.MAN), Filters.keyword(Keyword.RING_BOUND));

            List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
            for (PhysicalCard woundedRingBound : woundedRingBounds) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new AddUntilEndOfPhaseModifierEffect(
                                new CantTakeWoundsModifier(self, Filters.sameCard(woundedRingBound)), Phase.SKIRMISH));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP);
            action.appendCost(
                    new AddTwilightEffect(self, 2));
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Filters.race(Race.MAN), Filters.keyword(Keyword.RING_BOUND)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
