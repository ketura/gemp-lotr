package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: Bag End
 * Set: Second Edition
 * Side: None
 * Site Number: 1
 * Shadow Number: 0
 * Card Number: 1U273
 * Game Text: Dwelling. Fellowship: Discard a card from hand to make Frodo strength +1 until the end of turn.
 */
public class Card40_273 extends AbstractSite {
    public Card40_273() {
        super("Bag End", SitesBlock.SECOND_ED, 1, 0, Direction.LEFT);
        addKeyword(Keyword.DWELLING);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Filters.any));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Filters.frodo));
            return Collections.singletonList(action);
        }
        return null;
    }
}
