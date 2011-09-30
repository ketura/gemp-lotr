package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ChoiceCost;
import com.gempukku.lotro.cards.costs.DiscardCardsFromPlayCost;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.ChooseableCost;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Tale. Bearer must be Bilbo. Bilbo may not take wounds (except during the archery phase). Fellowship or
 * Regroup: Exert Bilbo or discard this condition to remove (1).
 */
public class Card2_097 extends AbstractAttachable {
    public Card2_097() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 0, Culture.SHIRE, null, "Consorting With Wizards");
        addKeyword(Keyword.TALE);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Bilbo");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new CantTakeWoundsModifier(self,
                        Filters.and(
                                Filters.hasAttached(self),
                                new Filter() {
                                    @Override
                                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                        return gameState.getCurrentPhase() != Phase.ARCHERY;
                                    }
                                })));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                || PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self)) {
            ActivateCardAction action = new ActivateCardAction(self, (game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP) ? Keyword.FELLOWSHIP : Keyword.REGROUP);
            List<ChooseableCost> possibleCosts = new LinkedList<ChooseableCost>();
            possibleCosts.add(
                    new ExertCharactersCost(self, self.getAttachedTo()));
            possibleCosts.add(
                    new DiscardCardsFromPlayCost(self));
            action.appendCost(
                    new ChoiceCost(action, playerId, possibleCosts));
            action.appendEffect(
                    new RemoveTwilightEffect(1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
