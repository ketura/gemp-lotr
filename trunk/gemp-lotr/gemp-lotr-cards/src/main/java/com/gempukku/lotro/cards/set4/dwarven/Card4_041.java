package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Gimli. He is damage +1. Skirmish: Discard a [DWARVEN] condition or a card stacked on
 * a [DWARVEN] condition to make Gimli strength +1.
 */
public class Card4_041 extends AbstractAttachableFPPossession {
    public Card4_041() {
        super(2, 2, 0, Culture.DWARVEN, PossessionClass.HAND_WEAPON, "Axe of Erebor", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Gimli");
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.CONDITION), Filters.culture(Culture.DWARVEN)) > 0) {
            ActivateCardAction action = new ActivateCardAction(self);

            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.type(CardType.CONDITION), Filters.culture(Culture.DWARVEN)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard DWARVEN condition";
                        }
                    });
            possibleCosts.add(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.and(Filters.type(CardType.CONDITION), Filters.culture(Culture.DWARVEN)), Filters.any) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard a card stacked on a DWARVEN condition";
                        }
                    });

            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));

            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.name("Gimli"), 1), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
