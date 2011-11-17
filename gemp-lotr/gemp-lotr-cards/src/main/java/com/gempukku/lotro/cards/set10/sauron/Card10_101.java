package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 7
 * Type: Minion • Troll
 * Strength: 14
 * Vitality: 4
 * Site: 6
 * Game Text: Damage +1. Fierce. To play, spot a [SAURON] minion. This minion is strength +1 for each possession you
 * can spot.
 */
public class Card10_101 extends AbstractMinion {
    public Card10_101() {
        super(7, 14, 4, 6, Race.TROLL, Culture.SAURON, "Troll of Cirith Gorgol");
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.SAURON, CardType.MINION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self, null, new CountSpottableEvaluator(CardType.POSSESSION)));
    }
}
