package com.gempukku.lotro.cards.set9.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Artifact • Ring
 * Strength: +1
 * Game Text: Bearer must be a Dwarf. Bearer is damage +1. Maneuver: For each Dwarf you spot, reveal a card from the
 * top of your draw deck. Take all Free Peoples cards revealed into hand and discard the rest. Discard this artifact.
 */
public class Card9_006 extends AbstractAttachableFPPossession {
    public Card9_006() {
        super(0, 1, 0, Culture.DWARVEN, CardType.ARTIFACT, PossessionClass.RING, "Ring of Accretion", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 1));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ForEachYouSpotEffect(playerId, Race.DWARF) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            action.appendEffect(
                                    new RevealTopCardsOfDrawDeckEffect(self, playerId, spotCount) {
                                        @Override
                                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                                            for (PhysicalCard card : revealedCards) {
                                                if (card.getBlueprint().getSide() == Side.FREE_PEOPLE)
                                                    action.appendEffect(
                                                            new PutCardFromDeckIntoHandEffect(card));
                                                else
                                                    action.appendEffect(
                                                            new DiscardCardFromDeckEffect(card));
                                            }
                                            action.appendEffect(
                                                    new SelfDiscardEffect(self));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
