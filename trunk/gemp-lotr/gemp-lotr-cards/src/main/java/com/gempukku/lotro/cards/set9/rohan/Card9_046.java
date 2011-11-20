package com.gempukku.lotro.cards.set9.rohan;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.ExtraFilters;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 0
 * Type: Artifact • Support Area
 * Game Text: Assignment: Exert a [GONDOR] Man to play a [ROHAN] Man. You may exert that [ROHAN] Man to play
 * a possession on him or her. Discard this artifact.
 */
public class Card9_046 extends AbstractPermanent {
    public Card9_046() {
        super(Side.FREE_PEOPLE, 0, CardType.ARTIFACT, Culture.ROHAN, Zone.SUPPORT, "The Red Arrow", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ASSIGNMENT, self)
                && PlayConditions.canExert(self, game, Culture.GONDOR, Race.MAN)
                && PlayConditions.canPlayFromHand(playerId, game, Culture.ROHAN, Race.MAN)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, Race.MAN));
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.ROHAN, Race.MAN) {
                        @Override
                        protected void afterCardPlayed(final PhysicalCard cardPlayed) {
                            if (PlayConditions.canExert(self, game, cardPlayed)
                                    && PlayConditions.canPlayFromHand(playerId, game, CardType.POSSESSION, ExtraFilters.attachableTo(game, cardPlayed))) {
                                action.insertEffect(
                                        new OptionalEffect(action, playerId,
                                                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, cardPlayed) {
                                                    @Override
                                                    public String getText(LotroGame game) {
                                                        return "Exert that ROHAN Man to play a possession on him or her";
                                                    }

                                                    @Override
                                                    protected void forEachCardExertedCallback(PhysicalCard character) {
                                                        action.insertEffect(
                                                                new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getHand(playerId), Filters.and(CardType.POSSESSION, ExtraFilters.attachableTo(game, cardPlayed)), 1, 1) {
                                                                    @Override
                                                                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                                                        if (selectedCards.size() > 0) {
                                                                            PhysicalCard selectedCard = selectedCards.iterator().next();
                                                                            AttachPermanentAction attachPermanentAction = ((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, Filters.and(cardPlayed), 0);
                                                                            game.getActionsEnvironment().addActionToStack(attachPermanentAction);
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }));
                            }
                        }
                    });
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
