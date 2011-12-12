package com.gempukku.lotro.cards.set12.orc;

import com.gempukku.lotro.cards.*;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
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
 * Set: Black Rider
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 1
 * Site: 4
 * Game Text: When you play this minion, you may play an [ORC] weapon from your discard pile on your [ORC] Orc.
 */
public class Card12_102 extends AbstractMinion {
    public Card12_102() {
        super(3, 8, 1, 4, Race.ORC, Culture.ORC, "Scavenging Goblins");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        final Filter attachableToFilter = Filters.and(Filters.owner(playerId), Culture.ORC, Race.ORC);
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.ORC, Filters.weapon, ExtraFilters.attachableTo(game, attachableToFilter))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDiscard(playerId),
                            Filters.and(
                                    Culture.ORC,
                                    Filters.weapon,
                                    ExtraFilters.attachableTo(game, attachableToFilter)), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                game.getActionsEnvironment().addActionToStack(((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, attachableToFilter, 0));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
