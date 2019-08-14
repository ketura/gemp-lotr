package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Title: Succumb to Despair
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 0
 * Type: Event - Response
 * Card Number: 1R201
 * Game Text: If a Nazgul wins a skirmish against a companion with 0 resistance or less, exert that Nazgul twice
 * to place that companion in the dead pile.
 */
public class Card40_201 extends AbstractResponseEvent {
    public Card40_201() {
        super(Side.SHADOW, 0, Culture.WRAITH, "Succumb to Despair");
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmishInvolving(game, effectResult, Race.NAZGUL, Filters.and(CardType.COMPANION, Filters.maxResistance(0)))
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            CharacterWonSkirmishResult wonSkirmishResult = (CharacterWonSkirmishResult) effectResult;
            final PhysicalCard winner = wonSkirmishResult.getWinner();
            if (PlayConditions.canExert(self, game, 2, winner)) {
                final Set<PhysicalCard> involving = wonSkirmishResult.getInvolving();
                final Collection<PhysicalCard> toKill = Filters.filter(involving, game, CardType.COMPANION, Filters.maxResistance(0));

                PlayEventAction action = new PlayEventAction(self);
                action.setText("Play exerting " + GameUtils.getFullName(winner));
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, winner));
                action.appendEffect(
                        new KillEffect(toKill, KillEffect.Cause.CARD_EFFECT));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
