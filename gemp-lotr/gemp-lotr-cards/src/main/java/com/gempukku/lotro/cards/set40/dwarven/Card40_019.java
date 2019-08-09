package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Gimli's Battle Axe, Weapon of Erebor
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Possession - Hand Weapon
 * Card Number: 1R19
 * Game Text: Bearer must be a Dwarf. If bearer is Gimli, he is damage +1.
 * Each time bearer wins a skirmish, you may stack a [DWARVEN] event from your discard pile on a [DWARVEN] support area
 * condition.
 */
public class Card40_019 extends AbstractAttachableFPPossession {
    public Card40_019() {
        super(2, 0, 0, Culture.DWARVEN, PossessionClass.HAND_WEAPON, "Gimli's Battle Axe", "Weapon of Erebor", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, Filters.and(Filters.name("Gimli"), Filters.hasAttached(self)), Keyword.DAMAGE, 1);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose condition to stack on", Culture.DWARVEN, CardType.CONDITION, Zone.SUPPORT) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new ChooseAndStackCardsFromDiscardEffect(
                                            action, playerId, 1, 1, card,
                                            Culture.DWARVEN, CardType.EVENT));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
