package com.gempukku.lotro.cards.set8.raider;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.conditions.InitiativeCondition;
import com.gempukku.lotro.cards.modifiers.conditions.MinThreatCondition;
import com.gempukku.lotro.cards.modifiers.conditions.OrCondition;
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
 * Twilight Cost: 3
 * Type: Possession • Mount
 * Strength: +3
 * Game Text: Bearer must be a Southron. Bearer is fierce. While you have initiative or can spot 4 threats, bearer
 * is damage +1.
 */
public class Card8_064 extends AbstractAttachable {
    public Card8_064() {
        super(Side.SHADOW, CardType.POSSESSION, 3, Culture.RAIDER, PossessionClass.MOUNT, "Mumakil");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.SOUTHRON;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 3));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.FIERCE));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self),
                        new OrCondition(
                                new InitiativeCondition(Side.SHADOW),
                                new MinThreatCondition(4)
                        ), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
