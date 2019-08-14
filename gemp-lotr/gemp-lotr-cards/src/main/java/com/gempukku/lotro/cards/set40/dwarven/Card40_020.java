package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Gloin, Venerable Dwarf
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Companion - Dwarf
 * Strength: 5
 * Vitality: 4
 * Resistance: 6
 * Card Number: 1R20
 * Game Text: Damage +1. While you can spot Gimli, Gloin is strength +1 and his twilight cost is -1.
 * Skirmish: Exert Gloin to make a Dwarf strength +2 (and damage +1 if that Dwarf is Gimli).
 */
public class Card40_020 extends AbstractCompanion {
    public Card40_020() {
        super(3, 5, 4, 6, Culture.DWARVEN, Race.DWARF, null, "Gloin", "Venerable Dwarf", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        if (Filters.canSpot(game, Filters.name("Gimli")))
            return -1;
        return 0;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, self,
                new SpotCondition(Filters.name("Gimli")), 1);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf", Race.DWARF) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, card, 2)));
                            if (Filters.name("Gimli").accepts(game, card)) {
                                action.appendEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new KeywordModifier(self, card, Keyword.DAMAGE, 1)));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
