package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.DiscardCardsFromPlayCost;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale. Bearer must be a Hobbit companion. Maneuver: Discard this condition to make each Hobbit companion
 * strength +2 until the regroup phase.
 */
public class Card1_317 extends AbstractAttachable {
    public Card1_317() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.SHIRE, null, "There and Back Again", true);
        addKeyword(Keyword.TALE);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.race(Race.HOBBIT);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER, "Discard this condition to make each Hobbit companion strength +2 until the regroup phase.");
            action.appendCost(new DiscardCardsFromPlayCost(self, self));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.and(Filters.type(CardType.COMPANION), Filters.race(Race.HOBBIT)), 2), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
