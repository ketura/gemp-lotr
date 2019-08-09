package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * The Prancing Pony
 * 1	0
 * Dwelling.
 * Fellowship: Add a burden to play Aragorn from your draw deck.
 */
public class Card20_419 extends AbstractSite {
    public Card20_419() {
        super("The Prancing Pony", Block.SECOND_ED, 1, 0, null);
        addKeyword(Keyword.DWELLING);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && GameUtils.isFP(game, playerId)
                && PlayConditions.canAddBurdens(game, playerId, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddBurdenEffect(playerId, self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.aragorn));
            return Collections.singletonList(action);
        }
        return null;
    }
}
