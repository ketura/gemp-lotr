package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.PutOnTheOneRingEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Search. Maneuver: Exert an Uruk-hai to make the opponent choose to either exert 2 companions or make the
 * Ring-bearer put on The One Ring until the regroup phase.
 */
public class Card1_137 extends AbstractEvent {
    public Card1_137() {
        super(Side.SHADOW, Culture.ISENGARD, "Saruman's Reach", Phase.MANEUVER);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.URUK_HAI));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.URUK_HAI)));

        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Filters.type(CardType.COMPANION)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Exert 2 companions";
                    }
                });

        possibleEffects.add(new PutOnTheOneRingEffect());

        action.appendEffect(
                new ChoiceEffect(action, game.getGameState().getCurrentPlayerId(), possibleEffects));
        return action;
    }
}
