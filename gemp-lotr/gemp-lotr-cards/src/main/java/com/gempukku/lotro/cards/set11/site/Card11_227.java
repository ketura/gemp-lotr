package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Shadows
 * Twilight Cost: 0
 * Type: Site
 * Game Text: River. The minion archery total is +2 for each companion in the fellowship over 4.
 */
public class Card11_227 extends AbstractNewSite {
    public Card11_227() {
        super("Anduin Banks", 0, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ArcheryTotalModifier(self, Side.SHADOW, null,
                new MultiplyEvaluator(2,
                        new CountActiveEvaluator(4, (Integer) null, CardType.COMPANION)));
    }
}
