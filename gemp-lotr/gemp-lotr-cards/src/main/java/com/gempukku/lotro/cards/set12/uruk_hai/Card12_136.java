package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Possession • Hand Weapon
 * Game Text: Toil 1. (For each [URUK-HAI] character you exert when playing this, its twilight cost is -1) Bearer must
 * be an [URUK-HAI] Uruk-hai. Bearer is strength +2 for each wound on each character in its skirmish.
 */
public class Card12_136 extends AbstractAttachable {
    public Card12_136() {
        super(Side.SHADOW, CardType.POSSESSION, 3, Culture.URUK_HAI, PossessionClass.HAND_WEAPON, "Berserker Torch");
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.URUK_HAI, Race.URUK_HAI);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Filters.hasAttached(self), Filters.inSkirmish), null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                        int wounds = 0;
                        for (PhysicalCard physicalCard : Filters.filterActive(gameState, modifiersQuerying, Filters.character, Filters.inSkirmish, Filters.wounded)) {
                            wounds += gameState.getWounds(physicalCard);
                        }
                        return wounds * 2;
                    }
                });
    }
}
