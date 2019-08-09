package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Ithilien Wildwood
 * 5	7
 * Forest.
 * Regroup: Discard X Southrons to make the Free Peoples player wound X companions.
 */
public class Card20_442 extends AbstractSite {
    public Card20_442() {
        super("Ithilien Wildwood", SitesBlock.SECOND_ED, 5, 7, null);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.REGROUP, self)
                && GameUtils.isShadow(game, playerId)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, Integer.MAX_VALUE, Filters.owner(playerId), Keyword.SOUTHRON) {
                        @Override
                        protected void forEachDiscardedByEffectCallback(Collection<PhysicalCard> cards) {
                            int count = cards.size();
                            if (count > 0)
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), count, count, CardType.COMPANION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
