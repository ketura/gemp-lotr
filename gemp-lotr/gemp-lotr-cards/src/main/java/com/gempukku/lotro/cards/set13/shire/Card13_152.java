package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 Hobbits. When you play this, add a [SHIRE] token here for each forest site and each
 * dwelling site on the adventure path. Skirmish: Discard this from play or remove 3 tokens from here to cancel
 * a skirmish involving a Hobbit.
 */
public class Card13_152 extends AbstractPermanent {
    public Card13_152() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Humble Homestead", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Race.HOBBIT);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.SITE, Zone.ADVENTURE_PATH, Filters.or(Keyword.FOREST, Keyword.DWELLING));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.SHIRE, count));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new RemoveTokenEffect(self, self, Token.SHIRE, 3));
            possibleCosts.add(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new CancelSkirmishEffect(Race.HOBBIT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
