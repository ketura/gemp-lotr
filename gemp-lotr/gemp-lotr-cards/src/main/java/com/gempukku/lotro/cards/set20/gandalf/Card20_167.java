package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 2
 * My Brave Hobbits
 * Gandalf	Event • Manuever
 * Spell.
 * To play, spot 2 Hobbit companions and exert Gandalf twice.
 * Until the regroup phase, Hobbits are strength +3 and take no more than one wound in a skirmish.
 */
public class Card20_167 extends AbstractEvent {
    public Card20_167() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "My Brave Hobbits", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, CardType.COMPANION, Race.HOBBIT)
                && PlayConditions.canExert(self, game, 2, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.gandalf));
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new StrengthModifier(self, Race.HOBBIT, 3), Phase.REGROUP));
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, Race.HOBBIT), Phase.REGROUP));
        return action;
    }
}
