package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.ExtraFilters;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 1
 * Site: 4
 * Game Text: When you play this minion, you may play a weapon from your discard pile on your [MORIA] Orc.
 */
public class Card1_179 extends AbstractMinion {
    public Card1_179() {
        super(3, 8, 1, 4, Race.ORC, Culture.MORIA, "Goblin Scavengers");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        final Filter additionalAttachmentFilter = Filters.and(Filters.owner(self.getOwner()), Culture.MORIA, Race.ORC);

        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))
                && PlayConditions.canPlayFromDiscard(playerId, game, Filters.weapon, ExtraFilters.attachableTo(game, additionalAttachmentFilter))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDiscard(playerId),
                            Filters.and(
                                    Filters.weapon,
                                    ExtraFilters.attachableTo(game, additionalAttachmentFilter)), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                game.getActionsEnvironment().addActionToStack(((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, additionalAttachmentFilter, 0));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}


