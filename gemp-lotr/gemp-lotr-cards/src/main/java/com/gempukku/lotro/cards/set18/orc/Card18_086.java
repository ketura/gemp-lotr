package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 10
 * Vitality: 3
 * Site: 4
 * Game Text: To play, spot an [ORC] card. The minion archery total is +1 for each hunter companion you can spot.
 */
public class Card18_086 extends AbstractMinion {
    public Card18_086() {
        super(5, 10, 3, 4, Race.ORC, Culture.ORC, "Orkish Archer Troop");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ORC);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ArcheryTotalModifier(self, Side.SHADOW, null, new CountActiveEvaluator(CardType.COMPANION, Keyword.HUNTER)));
}
}
