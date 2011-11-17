package com.gempukku.lotro.cards.set10.gollum;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.actions.SkirmishPhaseAction;
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
    public List<PlayEventAction> getOptionalAfterActions(String playerId, final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.POSSESSION)
                && checkPlayRequirements(playerId, game, self, 0, false)
                && (PlayConditions.canPlayFromDiscard(playerId, game, -2, Filters.gollum) || PlayConditions.canPlayFromHand(playerId, game, -2, Filters.gollum))) {
            PlayCardResult playResult = (PlayCardResult) effectResult;
            if (playResult.getPlayedCard().getOwner().equals(game.getGameState().getCurrentPlayerId())) {
                final PhysicalCard playedOn = playResult.getAttachedTo();
                if (playedOn != null && playedOn.getBlueprint().getCardType() == CardType.COMPANION) {
                    PlayEventAction action = new PlayEventAction(self);
                    List<Effect> possibleEffects = new LinkedList<Effect>();
                    possibleEffects.add(
                            new ChooseAndPlayCardFromDiscardEffect(playerId, game, -2, Filters.gollum) {
                                @Override
                                protected void afterCardPlayed(PhysicalCard cardPlayed) {
                                    gollumPlayed(game, playedOn, cardPlayed);
                                }
                            });
                    possibleEffects.add(
                            new ChooseAndPlayCardFromHandEffect(playerId, game, -2, Filters.gollum) {
                                @Override
                                protected void afterCardPlayed(PhysicalCard cardPlayed) {
                                    gollumPlayed(game, playedOn, cardPlayed);
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

    private void gollumPlayed(LotroGame game, PhysicalCard character, PhysicalCard gollum) {
        final Phase currentPhase = game.getGameState().getCurrentPhase();
        final Skirmish skirmish = game.getGameState().getSkirmish();
        if (skirmish != null)
            game.getGameState().finishSkirmish();

        final SkirmishPhaseAction skirmishPhaseAction = new SkirmishPhaseAction(character, Collections.singletonList(gollum));
        // We might have to restart the skirmish, if the possession was played during it
        if (skirmish != null) {
            skirmishPhaseAction.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            game.getGameState().restartSkirmish(skirmish);
                        }
                    });
        }
        // We have to reset the phase, to whatever it was before
        skirmishPhaseAction.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        game.getGameState().setCurrentPhase(currentPhase);
                    }
                });

        game.getActionsEnvironment().addActionToStack(
                skirmishPhaseAction);
    }
}
