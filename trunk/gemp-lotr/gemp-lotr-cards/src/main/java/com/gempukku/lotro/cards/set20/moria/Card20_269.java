package com.gempukku.lotro.cards.set20.moria;

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
 * 3
 * Goblin Scavengers
 * Moria	Minion • Goblin
 * 8	1	4
 * When you play this minion, you may play a weapon from your discard pile on your [Moria] Goblin.
 */
public class Card20_269 extends AbstractMinion {
    public Card20_269() {
        super(3, 8, 1, 4, Race.GOBLIN, Culture.MORIA, "Goblin Scavengers");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        final Filter additionalAttachmentFilter = Filters.and(Filters.owner(self.getOwner()), Culture.MORIA, Race.GOBLIN);

        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, Filters.weapon, ExtraFilters.attachableTo(game, additionalAttachmentFilter))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDiscard(playerId),
                            Filters.and(
                                    Filters.weapon,
                                    ExtraFilters.attachableTo(game, additionalAttachmentFilter)), 1, 1, true) {
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
