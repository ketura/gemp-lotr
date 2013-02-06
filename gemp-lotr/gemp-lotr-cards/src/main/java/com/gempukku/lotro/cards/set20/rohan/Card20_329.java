package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Hama, Protector of the Golden Hall
 * Rohan	Companion • Man
 * 6	3	5
 * Skirmish: Exert Hama to make him strength +1 for each mounted man.
 */
public class Card20_329 extends AbstractCompanion {
    public Card20_329() {
        super(2, 6, 3, 5, Culture.ROHAN, Race.MAN, null, "Hama", "Protector of the Golden Hall", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                            new CountActiveEvaluator(Race.MAN, Filters.mounted), self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
