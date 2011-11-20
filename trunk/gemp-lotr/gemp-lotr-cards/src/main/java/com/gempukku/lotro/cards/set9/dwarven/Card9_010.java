package com.gempukku.lotro.cards.set9.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: You may play [DWARVEN] skirmish events stacked on [DWARVEN] conditions as if from hand.
 */
public class Card9_010 extends AbstractCompanion {
    public Card9_010() {
        super(2, 6, 3, Culture.DWARVEN, Race.DWARF, null, "Sindri", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(final LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, null, Filters.and(Culture.DWARVEN, CardType.EVENT, Keyword.SKIRMISH, Filters.stackedOn(Culture.DWARVEN, CardType.CONDITION)), ModifierEffect.EXTRA_ACTION_MODIFIER) {
                    @Override
                    public List<? extends Action> getExtraPhaseActionFromStacked(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
                        if (card.getBlueprint().checkPlayRequirements(card.getOwner(), game, card, 0, false))
                            return Collections.singletonList(
                                    card.getBlueprint().getPlayCardAction(card.getOwner(), game, card, 0, false));
                        return null;
                    }
                });
    }
}
