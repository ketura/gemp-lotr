package com.gempukku.lotro.cards.set13.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Make Gollum strength +2 (or +4 if you can spot Smeagol or Deagol).
 */
public class Card13_051 extends AbstractEvent {
    public Card13_051() {
        super(Side.SHADOW, 0, Culture.GOLLUM, "It's My Birthday", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new ConditionEvaluator(2, 4, new SpotCondition(Filters.or(Filters.smeagol, Filters.name("Deagol")))), Filters.gollum));
        return action;
    }
}
