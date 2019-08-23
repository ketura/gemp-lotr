package com.gempukku.lotro.cards.set2.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Type: Site
 * Site: 1
 * Game Text: Fellowship: Play a [SHIRE] ally to draw a card.
 */
public class Card2_117 extends AbstractSite {
    public Card2_117() {
        super("Town Center", SitesBlock.FELLOWSHIP, 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && Filters.filter(game.getGameState().getHand(playerId), game, Culture.SHIRE, CardType.ALLY, Filters.playable(game)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Filters.and(Culture.SHIRE, CardType.ALLY)));
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
