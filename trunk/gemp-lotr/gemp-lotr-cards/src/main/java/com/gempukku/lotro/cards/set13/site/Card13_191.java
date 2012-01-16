package com.gempukku.lotro.cards.set13.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Set: Bloodlines
 * Twilight Cost: 2
 * Type: Site
 * Game Text: River. Each character is strength +1 for each card borne by that character.
 */
public class Card13_191 extends AbstractNewSite {
    public Card13_191() {
        super("Fords of Isen", 2, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.character, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                        return Filters.countActive(gameState, modifiersQuerying, Filters.attachedTo(cardAffected));
                    }
                });
    }
}
