package com.gempukku.lotro.cards.set13.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Gollum is strength +1 for each Ring-bound companion you can spot.
 */
public class Card13_044 extends AbstractPermanent {
    public Card13_044() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.GOLLUM, "Chasm's Edge");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, Filters.gollum, null, new CountActiveEvaluator(CardType.COMPANION, Keyword.RING_BOUND)));
}
}
