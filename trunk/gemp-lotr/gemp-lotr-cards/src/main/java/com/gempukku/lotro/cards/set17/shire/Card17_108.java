package com.gempukku.lotro.cards.set17.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Pipeweed. To play, spot an unbound Hobbit. Regroup: Discard a pipeweed from play to remove a threat.
 * Regroup: Discard a pipeweed from play to discard a minion.
 */
public class Card17_108 extends AbstractPermanent {
    public Card17_108() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Hornblower Leaf");
        addKeyword(Keyword.PIPEWEED);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.unboundCompanion, Race.HOBBIT);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canDiscardFromPlay(self, game, Keyword.PIPEWEED)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
            ActivateCardAction action1 = new ActivateCardAction(self);
            action1.setText("Remove a threat");
            action1.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action1, playerId, 1, 1, Keyword.PIPEWEED));
            action1.appendEffect(
                    new RemoveThreatsEffect(self, 1));
            actions.add(action1);

            ActivateCardAction action2 = new ActivateCardAction(self);
            action2.setText("Discard a minion");
            action2.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action2, playerId, 1, 1, Keyword.PIPEWEED));
            action2.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action2, playerId, 1, 1, CardType.MINION));
            actions.add(action2);
            return actions;
        }
        return null;
    }
}
