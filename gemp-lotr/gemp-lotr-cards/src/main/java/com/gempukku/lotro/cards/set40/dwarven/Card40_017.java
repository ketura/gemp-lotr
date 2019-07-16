package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.StackCardFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Gimli, Dwarven Emissary
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Companion - Dwarf
 * Strength: 6
 * Vitality: 4
 * Resistance: 7
 * Card Number: 1R17
 * Game Text: Damage +1. While in your starting fellowship, Gimli's twilight cost is -1.
 * At the beginning of the fellowship phase, you may stack a Free Peoples card from hand on a [DWARVEN] support area
 * condition to take a [DWARVEN] event from your discard pile into hand.
 */
public class Card40_017 extends AbstractCompanion{
    public Card40_017() {
        super(3, 6, 4, 7, Culture.DWARVEN, Race.DWARF, null, "Gimli", "Dwarven Emissary", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (gameState.getCurrentPhase() == Phase.PLAY_STARTING_FELLOWSHIP)
            return -1;
        return 0;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)
                && PlayConditions.canStackCardFromHand(self, game, playerId, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION, Zone.SUPPORT), Side.FREE_PEOPLE)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose condition to stack on", Culture.DWARVEN, CardType.CONDITION, Zone.SUPPORT) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendCost(
                                    new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, card, Side.FREE_PEOPLE));
                        }
                    });
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.DWARVEN, CardType.EVENT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
