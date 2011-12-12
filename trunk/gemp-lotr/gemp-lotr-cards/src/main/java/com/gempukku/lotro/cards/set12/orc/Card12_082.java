package com.gempukku.lotro.cards.set12.orc;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 5
 * Type: Event • Skirmish
 * Game Text: Toil 2. (For each [ORC] character you exert when playing this, its twilight cost is -2) Make each of your
 * [ORC] minions strength +2 until the regroup phase.
 */
public class Card12_082 extends AbstractEvent {
    public Card12_082() {
        super(Side.SHADOW, 5, Culture.ORC, "Barrage", Phase.SKIRMISH);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new StrengthModifier(self, Filters.and(Filters.owner(playerId), Culture.ORC, CardType.MINION), 2), Phase.REGROUP));
        return action;
    }
}
