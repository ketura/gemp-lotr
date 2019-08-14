package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * Succumb to Despair
 * Ringwraith	Event • Response
 * If a Nazgul wins a skirmish against a companion with 0 resistance or less,
 * exert that Nazgul twice to place that companion in the dead pile.
 */
public class Card20_302 extends AbstractResponseEvent {
    public Card20_302() {
        super(Side.SHADOW, 0, Culture.WRAITH, "Succumb to Despair");
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmishInvolving(game, effectResult, Race.NAZGUL, Filters.and(CardType.COMPANION, Filters.maxResistance(0)))) {
            final PhysicalCard winner = ((CharacterWonSkirmishResult) effectResult).getWinner();
            if (PlayConditions.canExert(self, game, 2, winner)) {
                PlayEventAction action = new PlayEventAction(self);
                action.setText("Play Succumb to Despair exerting " + GameUtils.getFullName(winner));
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, winner));
                PhysicalCard loser = Filters.findFirstActive(game, CardType.COMPANION, Filters.maxResistance(0), Filters.inSkirmish);
                if (loser != null)
                    action.appendEffect(
                            new KillEffect(loser, KillEffect.Cause.CARD_EFFECT));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
