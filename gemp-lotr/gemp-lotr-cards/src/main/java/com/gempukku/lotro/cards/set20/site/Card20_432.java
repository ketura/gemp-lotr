package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Gates of Moria
 * 3	1
 * Sanctuary. Underground.
 * Fellowship: Play a [Dwarven] possession to draw a card.
 */
public class Card20_432 extends AbstractSite {
    public Card20_432() {
        super("Gates of Moria", SitesBlock.SECOND_ED, 3, 1, null);
        addKeyword(Keyword.SANCTUARY);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && GameUtils.isFP(game, playerId)
                && PlayConditions.canPlayFromHand(playerId, game, Culture.DWARVEN, CardType.POSSESSION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.DWARVEN, CardType.POSSESSION));
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
