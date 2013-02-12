package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
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
 * Rohirrim Commoner
 * Rohan	Ally • Man • Edoras
 * 4	2
 * Villager.
 * Fellowship: Exert this ally to play a [Rohan] fortification from your discard pile.
 */
public class Card20_338 extends AbstractAlly {
    public Card20_338() {
        super(1, null, 0, 4, 2, Race.MAN, Culture.ROHAN, "Rohirrim Commoner");
        addKeyword(Keyword.EDORAS);
        addKeyword(Keyword.VILLAGER);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.ROHAN, Keyword.FORTIFICATION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.ROHAN, Keyword.FORTIFICATION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
