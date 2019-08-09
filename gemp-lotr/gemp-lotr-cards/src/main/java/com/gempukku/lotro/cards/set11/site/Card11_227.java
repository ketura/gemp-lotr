package com.gempukku.lotro.cards.set11.site;
import java.util.List;
import java.util.Collections;

import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 0
 * Type: Site
 * Game Text: River. The minion archery total is +2 for each companion in the fellowship over 4.
 */
public class Card11_227 extends AbstractShadowsSite {
    public Card11_227() {
        super("Anduin Banks", 0, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ArcheryTotalModifier(self, Side.SHADOW, null,
new MultiplyEvaluator(2,
new CountActiveEvaluator(4, (Integer) null, CardType.COMPANION))));
}
}
