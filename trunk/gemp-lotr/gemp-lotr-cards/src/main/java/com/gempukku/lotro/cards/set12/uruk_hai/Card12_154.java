package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 10
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Each time a companion is killed in a skirmish involving an [URUK-HAI] minion, you may discard
 * any number of cards from hand to draw the same number of cards.
 */
public class Card12_154 extends AbstractMinion {
    public Card12_154() {
        super(4, 10, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Slaughterer");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachKilledInASkirmish(game, effectResult, Filters.and(Culture.URUK_HAI, CardType.MINION), CardType.COMPANION)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 0, game.getGameState().getHand(playerId).size()) {
                        @Override
                        protected void cardsBeingDiscardedCallback(Collection<PhysicalCard> cardsBeingDiscarded) {
                            if (cardsBeingDiscarded.size() > 0) {
                                action.appendEffect(
                                        new DrawCardsEffect(playerId, cardsBeingDiscarded.size()));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
