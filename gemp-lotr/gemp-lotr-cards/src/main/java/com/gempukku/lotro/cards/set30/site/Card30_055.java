package com.gempukku.lotro.cards.set30.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Twilight Cost: 6
 * Type: Site
 * Site: 7
 * Game Text: Mountain. River. Shadow: Exert 2 minions and spot 6 companions to draw 2 cards.
 */
public class Card30_055 extends AbstractSite {
    public Card30_055() {
        super("Erebor", SitesBlock.HOBBIT, 7, 6, Direction.RIGHT);
		addKeyword(Keyword.MOUNTAIN);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && PlayConditions.canExert(self, game, 1, 2, CardType.MINION)
                && PlayConditions.canSpot(game, 6, CardType.COMPANION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, CardType.MINION));
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}