package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event • Response
 * Game Text: If the Free Peoples player plays a possession, discard a [SAURON] minion from hand to prevent him or her
 * from playing any more cards until the end of this phase.
 */
public class Card10_098 extends AbstractResponseEvent {
    public Card10_098() {
        super(Side.SHADOW, 0, Culture.SAURON, "Ruinous Hail");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.owner(game.getGameState().getCurrentPlayerId()), CardType.POSSESSION)
                && PlayConditions.canDiscardCardsFromHandToPlay(self, game, playerId, 1, Culture.SAURON, CardType.MINION)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.SAURON, CardType.MINION));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new AbstractModifier(self, null, null, ModifierEffect.ACTION_MODIFIER) {
                                @Override
                                public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
                                    if (action.getPerformingPlayer().equals(game.getGameState().getCurrentPlayerId())) {
                                        PhysicalCard source = action.getActionSource();
                                        if (source != null && (!source.getZone().isInPlay()))
                                            return false;
                                    }
                                    return true;
                                }
                            }, game.getGameState().getCurrentPhase()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
