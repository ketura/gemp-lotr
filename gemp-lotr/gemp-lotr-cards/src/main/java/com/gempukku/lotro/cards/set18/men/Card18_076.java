package com.gempukku.lotro.cards.set18.men;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.RemoveGameTextModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Event • Shadow
 * Game Text: Spot Grima to exert an unbound companion. That companion cannot use his or her game text until
 * the regroup phase.
 */
public class Card18_076 extends AbstractEvent {
    public Card18_076() {
        super(Side.SHADOW, 3, Culture.MEN, "Treachery", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.name("Grima"));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.unboundCompanion) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new RemoveGameTextModifier(self, character), Phase.REGROUP));
                    }
                });
        return action;
    }
}
