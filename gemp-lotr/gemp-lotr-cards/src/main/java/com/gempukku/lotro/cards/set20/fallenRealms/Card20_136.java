package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * Southron Bandit
 * Fallen Realms	Minion • Man
 * 8	1	4
 * Southron.
 * Regroup: Remove (5) to discard a Free Peoples possession (or all free people's possessions if you can spot 6 companions).
 */
public class Card20_136 extends AbstractMinion {
    public Card20_136() {
        super(3, 8, 1, 4, Race.MAN, Culture.FALLEN_REALMS, "Southron Bandit");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 5)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(5));
            boolean all = Filters.countSpottable(game, CardType.COMPANION)>=6;
            if (all)
                action.appendEffect(
                        new DiscardCardsFromPlayEffect(self.getOwner(), self, Side.FREE_PEOPLE, CardType.POSSESSION));
            else
                action.appendEffect(
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
