package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndStackCardsFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Gimli's Battle Axe, Weapon of Erebor
 * Dwarven	Possession • Hand Weapon
 * 2	1
 * Bearer must be a Dwarf.
 * If bearer is Gimli, he is damage +1.
 * Each time bearer wins a skirmish, you may stack a [Dwarven] event from your discard pile on a [Dwarven] support
 * area condition.
 */
public class Card20_055 extends AbstractAttachableFPPossession {
    public Card20_055() {
        super(2, 2, 1, Culture.DWARVEN, PossessionClass.HAND_WEAPON, "Gimli's Battle Axe", "Weapon of Erebor", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.gimli), Keyword.DAMAGE, 1));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a DWARVEN condition in your support area", Culture.DWARVEN, Zone.SUPPORT, CardType.CONDITION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new ChooseAndStackCardsFromDiscardEffect(action, playerId, 1, 1, card, Culture.DWARVEN, CardType.EVENT));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
