package com.gempukku.lotro.cards.set8.raider;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.conditions.InitiativeCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 0
 * Type: Possession • Ranged Weapon
 * Strength: +2
 * Game Text: Bearer must be a corsair. While you have initiative, bearer is an archer and fierce.
 */
public class Card8_052 extends AbstractAttachable {
    public Card8_052() {
        super(Side.SHADOW, CardType.POSSESSION, 0, Culture.RAIDER, PossessionClass.RANGED_WEAPON, "Corsair Ballista");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.CORSAIR;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), new InitiativeCondition(Side.SHADOW), Keyword.ARCHER, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), new InitiativeCondition(Side.SHADOW), Keyword.FIERCE, 1));
        return modifiers;
    }
}
