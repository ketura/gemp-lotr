package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.FPCulturesSpotCountModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 2
 * Fall of Free Peoples
 * Sauron	Event • Maneuver
 * To play, exert a [Sauron] Orc. Until the regroup phase, the number of Free Peoples cultures you can spot is -1.
 */
public class Card20_356 extends AbstractEvent {
    public Card20_356() {
        super(Side.SHADOW, 2, Culture.SAURON, "Fall of Free Peoples", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.SAURON, Race.ORC);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.SAURON, Race.ORC));
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new FPCulturesSpotCountModifier(self, playerId, -1), Phase.REGROUP));
        return action;
    }
}
