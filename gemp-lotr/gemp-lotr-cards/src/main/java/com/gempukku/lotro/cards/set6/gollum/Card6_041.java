package com.gempukku.lotro.cards.set6.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 5
 * Type: Event
 * Game Text: Regroup: Exert Gollum 3 times to wound each companion. The move limit for this turn is -1.
 */
public class Card6_041 extends AbstractEvent {
    public Card6_041() {
        super(Side.SHADOW, 5, Culture.GOLLUM, "Master Broke His Promise", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, 3, Filters.gollum);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 3, Filters.gollum));
        action.appendEffect(
                new WoundCharactersEffect(self, CardType.COMPANION));
        action.appendEffect(
                new AddUntilEndOfTurnModifierEffect(
                        new MoveLimitModifier(self, -1)));
        return action;
    }
}
