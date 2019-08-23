package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: Bywater Bridge
 * Set: Second Edition
 * Side: None
 * Site Number: 1
 * Shadow Number: 0
 * Card Number: 1C274
 * Game Text: River. Fellowship: Exert two Hobbits to play Gandalf from your draw deck.
 */
public class Card40_274 extends AbstractSite {
    public Card40_274() {
        super("Bywater Bridge", SitesBlock.SECOND_ED, 1, 0, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, 1, 2, Race.HOBBIT)
                && PlayConditions.canPlayFromDeck(playerId, game, Filters.gandalf)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Race.HOBBIT));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.gandalf));
            return Collections.singletonList(action);
        }
        return null;
    }
}
