package com.gempukku.lotro.cards.set13.gollum;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCardsFromDiscardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.*;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Minion
 * Strength: 5
 * Vitality: 4
 * Site: 3
 * Game Text: While a card titled Deagol is removed from the game, Gollum is strength +2 and fierce. Shadow: Remove
 * a card titled Deagol in your discard pile from the game to play this minion from your discard pile.
 */
public class Card13_050 extends AbstractMinion {
    public Card13_050() {
        super(2, 5, 4, 3, null, Culture.GOLLUM, "Gollum", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                for (String somePlayerId : gameState.getPlayerOrder().getAllPlayers()) {
                                    if (Filters.filter(gameState.getRemoved(somePlayerId), gameState, modifiersQuerying, Filters.name("Deagol")).size() > 0)
                                        return true;
                                }

                                return false;
                            }
                        }, 2));
        modifiers.add(
                new KeywordModifier(self, self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                for (String somePlayerId : gameState.getPlayerOrder().getAllPlayers()) {
                                    if (Filters.filter(gameState.getRemoved(somePlayerId), gameState, modifiersQuerying, Filters.name("Deagol")).size() > 0)
                                        return true;
                                }

                                return false;
                            }
                        }, Keyword.FIERCE, 1));
        return modifiers;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.SHADOW)
                && PlayConditions.canRemoveFromDiscard(self, game, playerId, 1, Filters.name("Deagol"))
                && PlayConditions.canPlayFromDiscard(playerId, game, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCardsFromDiscardEffect(action, self, playerId, 1, 1, Filters.name("Deagol")));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
