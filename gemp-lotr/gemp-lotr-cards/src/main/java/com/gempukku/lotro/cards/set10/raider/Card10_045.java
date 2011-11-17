package com.gempukku.lotro.cards.set10.raider;

import com.gempukku.lotro.cards.*;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. When you play this minion, you may play a mount on him from your discard pile. Its twilight cost
 * is -2.
 */
public class Card10_045 extends AbstractMinion {
    public Card10_045() {
        super(5, 11, 3, 4, Race.MAN, Culture.RAIDER, "Mumak Chieftain");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, -2, PossessionClass.MOUNT, ExtraFilters.attachableTo(game, self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDiscard(playerId), Filters.and(PossessionClass.MOUNT, ExtraFilters.attachableTo(game, -2, self)), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                AttachPermanentAction attachPermanentAction = ((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, Filters.and(self), -2);
                                game.getActionsEnvironment().addActionToStack(attachPermanentAction);
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
