package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Search. To play, exert a [SAURON] Orc. Plays to your support area. While you can spot 5 companions, each
 * companion's twilight cost is +2.
 */
public class Card1_253 extends AbstractPermanent {
    public Card1_253() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.SAURON, "Journey Into Danger");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Culture.SAURON, Race.ORC));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new TwilightCostModifier(self, CardType.COMPANION, new SpotCondition(5, CardType.COMPANION), 2));
}
}
