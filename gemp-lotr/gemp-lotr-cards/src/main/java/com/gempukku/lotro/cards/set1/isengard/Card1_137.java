package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.PutOnTheOneRingEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
        super(Side.SHADOW, 0, Culture.ISENGARD, "Saruman's Reach", Phase.MANEUVER);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.URUK_HAI);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.URUK_HAI));

        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 2, 2, CardType.COMPANION) {
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
