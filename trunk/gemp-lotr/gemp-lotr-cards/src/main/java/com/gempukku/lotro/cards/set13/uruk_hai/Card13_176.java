package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.ExtraFilters;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;

import java.util.Collection;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Exert your [URUK-HAI] lurker minion to play an [URUK-HAI] hand weapon from your discard pile on your
 * [URUK-HAI] minion.
 */
public class Card13_176 extends AbstractEvent {
    public Card13_176() {
        super(Side.SHADOW, 1, Culture.URUK_HAI, "War Machine", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Filters.owner(playerId), Culture.URUK_HAI, CardType.MINION, Keyword.LURKER)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.URUK_HAI, PossessionClass.HAND_WEAPON, ExtraFilters.attachableTo(game, Filters.owner(playerId), Culture.URUK_HAI, CardType.MINION));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final Filter targetFilter = Filters.and(Filters.owner(playerId), Culture.URUK_HAI, CardType.MINION);
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.owner(playerId), Culture.URUK_HAI, CardType.MINION, Keyword.LURKER));
        action.appendEffect(
                new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDiscard(playerId),
                        Filters.and(
                                Culture.URUK_HAI, PossessionClass.HAND_WEAPON,
                                ExtraFilters.attachableTo(game, targetFilter)), 1, 1) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                        if (selectedCards.size() > 0) {
                            PhysicalCard selectedCard = selectedCards.iterator().next();
                            game.getActionsEnvironment().addActionToStack(((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, targetFilter, 0));
                        }
                    }
                });
        return action;
    }
}
