package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.cards.modifiers.conditions.PhaseCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Possession • Shield
 * Vitality: +2
 * Game Text: Bearer must be a [MEN] minion. The twilight cost of this possession is -1 for each Free Peoples possession
 * you spot. Bearer cannot take wounds except during a skirmish.
 */
public class Card11_094 extends AbstractAttachable {
    public Card11_094() {
        super(Side.SHADOW, CardType.POSSESSION, 3, Culture.MEN, PossessionClass.SHIELD, "Pavise");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.MEN, CardType.MINION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new CantTakeWoundsModifier(self, new NotCondition(new PhaseCondition(Phase.SKIRMISH)), Filters.hasAttached(self)));
        return modifiers;
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -Filters.countActive(gameState, modifiersQuerying, Side.FREE_PEOPLE, CardType.POSSESSION);
    }
}
