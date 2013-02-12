package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Robin Smallburrow, Shirrif of Hobbiton
 * Shire	Ally • Hobbit • Shire
 * 2	2
 * Skirmish: At sites 1-4, exert Robin Smallburrow to cancel a skirmish involving a Hobbit.
 * Skirmish: At sites 5-9, exert Robin Smallburrow to make a Hobbit strength +2.
 */
public class Card20_405 extends AbstractAlly {
    public Card20_405() {
        super(1, null, 0, 2, 2, Race.HOBBIT, Culture.SHIRE, "Robin Smallburrow", "Shirrif of Hobbiton", true);
        addKeyword(Keyword.SHIRE);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            if (game.getGameState().getCurrentSiteNumber() <5)
                action.appendEffect(
                        new CancelSkirmishEffect(Race.HOBBIT));
            else
                action.appendEffect(
                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.HOBBIT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
