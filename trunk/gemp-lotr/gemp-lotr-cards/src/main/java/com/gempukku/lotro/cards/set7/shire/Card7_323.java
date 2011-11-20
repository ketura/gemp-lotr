package com.gempukku.lotro.cards.set7.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.MaxThreatCondition;
import com.gempukku.lotro.cards.modifiers.evaluator.ForEachThreatEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: While you cannot spot 4 threats, Pippin is strength +1 for each threat you can spot.
 */
public class Card7_323 extends AbstractCompanion {
    public Card7_323() {
        super(1, 3, 4, 6, Culture.SHIRE, Race.HOBBIT, Signet.GANDALF, "Pippin", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self, new MaxThreatCondition(3), new ForEachThreatEvaluator()));
    }
}
