package com.gempukku.lotro.cards.set10.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.AdditionalSkirmishPhaseEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Response
 * Game Text: If an opponent plays a possession on a companion, play Gollum at twilight cost -2 from your discard pile
 * or hand to suspend the current phase. Begin a skirmish phase involving Gollum and that companion. When it ends,
 * resume the suspended phase.
 */
public class Card10_019 extends AbstractResponseEvent {
    public Card10_019() {
        super(Side.SHADOW, 0, Culture.GOLLUM, "A Dark Shape Sprang");
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.POSSESSION)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)
                && (PlayConditions.canPlayFromDiscard(playerId, game, -2, Filters.gollum) || PlayConditions.canPlayFromHand(playerId, game, -2, Filters.gollum))) {
            PlayCardResult playResult = (PlayCardResult) effectResult;
            if (playResult.getPlayedCard().getOwner().equals(game.getGameState().getCurrentPlayerId())) {
                final PhysicalCard playedOn = playResult.getAttachedTo();
                if (playedOn != null && playedOn.getBlueprint().getCardType() == CardType.COMPANION) {
                    final PlayEventAction action = new PlayEventAction(self);
                    List<Effect> possibleEffects = new LinkedList<Effect>();
                    possibleEffects.add(
                            new ChooseAndPlayCardFromDiscardEffect(playerId, game, -2, Filters.gollum) {
                                @Override
                                protected void afterCardPlayed(PhysicalCard cardPlayed) {
                                    action.appendEffect(
                                            new AdditionalSkirmishPhaseEffect(playedOn, Collections.singleton(cardPlayed)));
                                }
                            });
                    possibleEffects.add(
                            new ChooseAndPlayCardFromHandEffect(playerId, game, -2, Filters.gollum) {
                                @Override
                                protected void afterCardPlayed(PhysicalCard cardPlayed) {
                                    action.appendEffect(
                                            new AdditionalSkirmishPhaseEffect(playedOn, Collections.singleton(cardPlayed)));
                                }
                            });
                    action.appendEffect(
                            new ChoiceEffect(action, playerId, possibleEffects));
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }
}
