package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

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
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.SHIRE, "Humble Homestead", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Race.HOBBIT);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int count = Filters.countActive(game, CardType.SITE, Zone.ADVENTURE_PATH, Filters.or(Keyword.FOREST, Keyword.DWELLING));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.SHIRE, count));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<>();
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
