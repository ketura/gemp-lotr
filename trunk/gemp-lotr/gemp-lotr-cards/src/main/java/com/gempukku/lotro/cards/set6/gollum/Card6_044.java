package com.gempukku.lotro.cards.set6.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 5
 * Type: Event
 * Game Text: Regroup: Exert Smeagol 3 times and discard him to wound each minion. The move limit for this turn is +1.
 * If the fellowship moves, each Shadow player may take up to 3 Shadow cards into hand from his or her discard pile.
 */
public class Card6_044 extends AbstractEvent {
    public Card6_044() {
        super(Side.FREE_PEOPLE, 5, Culture.GOLLUM, "Safe Paths", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game, 3, Filters.smeagol)
                && PlayConditions.canDiscardFromPlay(self, game, Filters.smeagol);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 3, Filters.smeagol));
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.smeagol));
        action.appendEffect(
                new WoundCharactersEffect(self, CardType.MINION));
        action.appendEffect(
                new AddUntilEndOfTurnModifierEffect(
                        new MoveLimitModifier(self, 1)));
        action.appendEffect(
                new AddUntilEndOfPhaseActionProxyEffect(
                        new AbstractActionProxy() {
                            @Override
                            public List<? extends Action> getOptionalAfterTriggers(String playerId, LotroGame lotroGame, EffectResult effectResults) {
                                if (effectResults.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES
                                        && !lotroGame.getGameState().getCurrentPlayerId().equals(playerId)) {
                                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                                    action.setActionAttachedToCard(lotroGame.getGameState().getCurrentSite());
                                    action.appendEffect(
                                            new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 0, 3, Side.SHADOW));
                                    return Collections.singletonList(action);
                                }
                                return null;
                            }
                        }, Phase.REGROUP));
        return action;
    }
}
