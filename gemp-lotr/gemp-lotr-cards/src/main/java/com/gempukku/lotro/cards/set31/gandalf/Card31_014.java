package com.gempukku.lotro.cards.set31.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndTransferAttachableEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * The Eagles Are Coming [Gandalf]
 * Event • Skirmish
 * Twilight Cost 3
 * 'Spell.
 * Exert Gandalf to play a [Gandalf] follower from your draw deck or discard pile.
 * You may attach a [Gandalf] follower from your support area to a companion (without paying the aid cost).'
 */
public class Card31_014 extends AbstractEvent {
    public Card31_014() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "The Eagles Are Coming", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Filters.name("Gandalf"));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);

        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Gandalf")));

        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.GANDALF, CardType.FOLLOWER));
        possibleEffects.add(
                new ChooseAndPlayCardFromDeckEffect(playerId, Culture.GANDALF, CardType.FOLLOWER));

        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        action.appendEffect(
                new OptionalEffect(action, playerId,
                        new ChooseAndTransferAttachableEffect(action, playerId, true, Filters.and(Culture.GANDALF, CardType.FOLLOWER, Zone.SUPPORT), Filters.any, CardType.COMPANION)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Attach a [GANDALF] follower";
                    }
                });

        return action;
    }
}
