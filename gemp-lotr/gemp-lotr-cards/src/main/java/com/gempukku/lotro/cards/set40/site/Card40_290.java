package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: Moria Caverns
 * Set: Second Edition
 * Side: None
 * Site Number: 4
 * Shadow Number: 4
 * Card Number: 1U290
 * Game Text: Underground. Shadow: Discard 2 cards from hand to play a Goblin from your discard pile.
 */
public class Card40_290 extends AbstractSite {
    public Card40_290() {
        super("Moria Caverns", Block.SECOND_ED, 4, 4, Direction.LEFT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && GameUtils.isShadow(game, playerId)
                && PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)
                && PlayConditions.canPlayFromDiscard(playerId, game, Race.GOBLIN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2, Filters.any));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
