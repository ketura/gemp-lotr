package com.gempukku.lotro.cards.set13.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Twilight Cost: 2
 * Type: Site
 * Game Text: River. Each character is strength +1 for each card borne by that character.
 */
public class Card13_191 extends AbstractShadowsSite {
    public Card13_191() {
        super("Fords of Isen", 2, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self, Filters.character, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        return Filters.countActive(game, Filters.attachedTo(cardAffected));
                    }
                }));
    }
}
