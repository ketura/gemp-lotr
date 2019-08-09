package com.gempukku.lotro.cards.set1.sauron;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 4
 * Site: 6
 * Game Text: For each companion you can spot, this minion is strength +1.
 */
public class Card1_256 extends AbstractMinion {
    public Card1_256() {
        super(5, 9, 4, 6, Race.ORC, Culture.SAURON, "Morgul Hunter");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, null, new CountActiveEvaluator(CardType.COMPANION)));
}
}
