package com.gempukku.lotro.cards.set6.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.PutCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.PutCardFromHandOnTopOfDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: While this card is stacked on a [DWARVEN] condition, Gimli is strength +1. Fellowship: Place this card
 * or another [DWARVEN] card from hand on top of or beneath your draw deck.
 */
public class Card6_011 extends AbstractEvent {
    public Card6_011() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Toss Me", Phase.FELLOWSHIP);
    }

    @Override
    public List<? extends Modifier> getStackedOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.name("Gimli"),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return Filters.and(Culture.DWARVEN, CardType.CONDITION).accepts(gameState, modifiersQuerying, self.getStackedOn());
                            }
                        }, 1));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new MultipleChoiceAwaitingDecision(1, "Where to put \"Toss Me\"?", new String[]{"Top of deck", "Bottom of deck"}) {
                            @Override
                            protected void validDecisionMade(int index, String result) {
                                action.skipDiscardPart();
                                if (index == 0)
                                    game.getGameState().putCardOnTopOfDeck(self);
                                else
                                    game.getGameState().putCardOnBottomOfDeck(self);
                            }
                        }) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Place played \"Toss Me\" on top or bottom of deck";
                    }
                });
        possibleEffects.add(
                new ChooseCardsFromHandEffect(playerId, 1, 1, Culture.DWARVEN) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Place other DWARVEN card from hand on top or bottom of deck";
                    }

                    @Override
                    protected void cardsSelected(LotroGame game, final Collection<PhysicalCard> selectedCards) {
                        action.insertEffect(
                                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                                        new MultipleChoiceAwaitingDecision(1, "Where to put selected card?", new String[]{"Top of deck", "Bottom of deck"}) {
                                            @Override
                                            protected void validDecisionMade(int index, String result) {
                                                if (index == 0)
                                                    for (PhysicalCard selectedCard : selectedCards)
                                                        action.insertEffect(
                                                                new PutCardFromHandOnTopOfDeckEffect(selectedCard));
                                                else
                                                    for (PhysicalCard selectedCard : selectedCards)
                                                        action.insertEffect(
                                                                new PutCardFromHandOnBottomOfDeckEffect(selectedCard));
                                            }
                                        })
                        );
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));

        return action;
    }
}
