package com.gempukku.lotro.cards.set21.moria;

import com.gempukku.lotro.cards.*;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 1
 * Site: 4
 * Game Text: When you play this minion, you may play a possession from your discard pile on your minion.
 */
public class Card21_42 extends AbstractMinion {
    public Card21_42() {
        super(3, 8, 1, 4, Race.ORC, Culture.MORIA, "Goblin Footman");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {

        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, CardType.POSSESSION, ExtraFilters.attachableTo(game, Filters.owner(self.getOwner())))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDiscard(playerId),
                            ExtraFilters.attachableTo(game, Filters.owner(self.getOwner()))), 1, 1, true) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                game.getActionsEnvironment().addActionToStack(((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, Filters.owner(self.getOwner()), 0));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}