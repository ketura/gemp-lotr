package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Orthanc Armorer
 * Isengard	Minion • Orc
 * 5	3	4
 * Lurker.
 * Skirmish: Exert this minion to play an [Isengard] Possession from your discard pile.
 */
public class Card20_210 extends AbstractMinion {
    public Card20_210() {
        super(2, 5, 3, 4, Race.ORC, Culture.ISENGARD, "Orthanc Armorer");
        addKeyword(Keyword.LURKER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.ISENGARD, CardType.POSSESSION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.ISENGARD, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
