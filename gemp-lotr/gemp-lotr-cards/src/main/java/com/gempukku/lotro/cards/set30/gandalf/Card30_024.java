package com.gempukku.lotro.cards.set30.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Spell. Exert Gandalf twice to wound a minion twice or discard a Troll.
 */
public class Card30_024 extends AbstractEvent {
    public Card30_024() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Dawn Take You All", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, 2, Filters.gandalf);
	}

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.gandalf));
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, 2, CardType.MINION) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Wound a minion twice";
                    }
                });
		possibleEffects.add(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Race.TROLL) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Discard a Troll";
                    }
				});
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
	}
}

		