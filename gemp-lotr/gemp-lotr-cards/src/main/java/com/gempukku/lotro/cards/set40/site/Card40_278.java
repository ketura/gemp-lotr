package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
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
 * Game Text: At the start of your turn, you may exert a Hobbit to play a pipe or pipeweed possession from your draw deck.
 */
public class Card40_278 extends AbstractSite {
    public Card40_278() {
        super("Southfarthing", SitesBlock.SECOND_ED, 1, 0, Direction.LEFT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)
                && GameUtils.isFP(game, playerId)
                && PlayConditions.canExert(self, game, Race.HOBBIT)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.or(PossessionClass.PIPE, Filters.and(CardType.POSSESSION, Keyword.PIPEWEED))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
