package com.gempukku.lotro.cards.set30.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, 2, Filters.gandalf);
	}

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.gandalf));
        List<Effect> possibleEffects = new LinkedList<Effect>();
        if (Filters.canSpot(game, CardType.MINION, Filters.moreVitalityThan(1))) {
            possibleEffects.add(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, 2, CardType.MINION, Filters.moreVitalityThan(1)) {
                @Override
                public String getText(LotroGame game) {
                    return "Wound a minion twice";
                }
            });
        } else if (!Filters.canSpot(game, Race.TROLL)) {
            possibleEffects.add(
                    new ChooseActiveCardEffect(self, playerId, "Choose minion", CardType.MINION, Filters.canTakeWounds(self, 1)) {
                @Override
                protected void cardSelected(LotroGame game, PhysicalCard card) {
                    action.insertEffect(
                            new WoundCharactersEffect(self, card));
                    action.insertEffect(
                            new WoundCharactersEffect(self, card));
                }
            });
        }
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

		
