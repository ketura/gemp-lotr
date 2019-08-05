package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Boromir, Son of Denethor
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion - Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Card Number: 1U102
 * Game Text: Skirmish: Exert Boromir to make a Hobbit strength +3.
 */
public class Card40_102 extends AbstractCompanion {
    public Card40_102() {
        super(3, 7, 3, 6, Culture.GONDOR, Race.MAN, null, "Boromir", "Son of Denethor", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 3, Race.HOBBIT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
