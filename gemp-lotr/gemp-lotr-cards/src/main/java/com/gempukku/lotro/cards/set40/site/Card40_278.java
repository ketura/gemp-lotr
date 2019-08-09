package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.FinishedPlayingFellowshipResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Southfarthing
 * Set: Second Edition
 * Side: None
 * Site Number: 1
 * Shadow Number: 0
 * Card Number: 1U278
 * Game Text: After revealing his or her starting fellowship, the first player may play a pipe or pipeweed possession
 * from his or her draw deck for free.
 */
public class Card40_278 extends AbstractSite {
    public Card40_278() {
        super("Southfarthing", Block.SECOND_ED, 1, 0, Direction.LEFT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.FINISHED_PLAYING_FELLOWSHIP) {
            FinishedPlayingFellowshipResult finishedPlayingFellowshipResult = (FinishedPlayingFellowshipResult) effectResult;
            if (finishedPlayingFellowshipResult.getPlayerId().equals(game.getGameState().getPlayerOrder().getFirstPlayer())
                    && PlayConditions.canPlayFromDeck(playerId, game, Filters.or(PossessionClass.PIPE, Filters.and(CardType.POSSESSION, Keyword.PIPEWEED)))) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new ChooseAndPlayCardFromDeckEffect(playerId, -100, Filters.or(PossessionClass.PIPE, Filters.and(CardType.POSSESSION, Keyword.PIPEWEED))));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
