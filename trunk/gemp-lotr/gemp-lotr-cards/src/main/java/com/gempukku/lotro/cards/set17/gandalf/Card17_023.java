package com.gempukku.lotro.cards.set17.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDeckIntoHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Spell. Fellowship: Exert your Wizard and choose an opponent to name either Free Peoples or Shadow. Reveal
 * the top three cards of your deck. If you revealed at least two of the chosen card type, take those cards into hand,
 * otherwise, discard those cards.
 */
public class Card17_023 extends AbstractPermanent {
    public Card17_023() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "Scintillating Bird");
        addKeyword(Keyword.SPELL);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Filters.owner(playerId), Race.WIZARD)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.owner(playerId), Race.WIZARD));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.appendEffect(
                                    new PlayoutDecisionEffect(
                                            opponentId,
                                            new MultipleChoiceAwaitingDecision(1, "Choose one", new String[]{"Free Peoples", "Shadow"}) {
                                                @Override
                                                protected void validDecisionMade(final int index, String result) {
                                                    action.appendEffect(
                                                            new RevealTopCardsOfDrawDeckEffect(self, playerId, 3) {
                                                                @Override
                                                                protected void cardsRevealed(List<PhysicalCard> cards) {
                                                                    Side side = (index == 0) ? Side.FREE_PEOPLE : Side.SHADOW;
                                                                    if (Filters.filter(cards, game.getGameState(), game.getModifiersQuerying(), side).size() >= 2) {
                                                                        putCardsIntoHandFromDeck(action, playerId, game, cards);
                                                                    } else {
                                                                        action.appendEffect(
                                                                                new DiscardTopCardFromDeckEffect(self, playerId, cards.size(), false));
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                    ));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    private void putCardsIntoHandFromDeck(ActivateCardAction action, String playerId, LotroGame game, List<PhysicalCard> cards) {
        int count = cards.size();
        for (int i = 0; i < count; i++) {
            action.appendEffect(
                    new ChooseAndPutCardFromDeckIntoHandEffect(action, playerId, 1, 1, Filters.in(cards)));
        }
    }
}
