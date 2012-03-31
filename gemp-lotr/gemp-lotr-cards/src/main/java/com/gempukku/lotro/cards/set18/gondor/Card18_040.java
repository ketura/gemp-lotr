package com.gempukku.lotro.cards.set18.gondor;

import com.gempukku.lotro.cards.*;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 5
 * Game Text: Ranger. When you play Boromir (except in your starting fellowship), you may play a possession with
 * a twilight cost of 1 or less on him from your draw deck. Reshuffle your draw deck.
 */
public class Card18_040 extends AbstractCompanion {
    public Card18_040() {
        super(3, 7, 3, 5, Culture.GONDOR, Race.MAN, null, "Boromir", "Proud and Noble Man", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && !PlayConditions.isPhase(game, Phase.PLAY_STARTING_FELLOWSHIP)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseCardsFromDeckEffect(playerId, 0, 1, CardType.POSSESSION, Filters.maxPrintedTwilightCost(1), ExtraFilters.attachableTo(game, self)) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                game.getActionsEnvironment().addActionToStack(((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, self, 0));
                            }
                        }
                    });
            action.appendEffect(
                    new ShuffleDeckEffect(playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
