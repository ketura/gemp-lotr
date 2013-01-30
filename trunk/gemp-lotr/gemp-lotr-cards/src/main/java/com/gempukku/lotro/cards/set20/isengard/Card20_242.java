package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

/**
 * 1
 * Wizard’s Guile
 * Isengard	Event • Maneuver
 * Spell.
 * Until the regroup phase, Saruman may only take wounds during skirmishes.
 */
public class Card20_242 extends AbstractEvent {
    public Card20_242() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "Wizard's Guile", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new CantTakeWoundsModifier(self, new NotCondition(new PhaseCondition(Phase.SKIRMISH)), Filters.saruman), Phase.REGROUP));
        return action;
    }
}
