package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.condition.NotCondition;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 0 Discipline [Fal]
 * Event • Shadow
 * To play, exert an Easterling.
 * Until the regroup phase, Easterlings may only take wounds during skirmishes.
 * http://lotrtcg.org/coreset/fallenrealms/discipline(r3).jpg
 */
public class Card20_109 extends AbstractEvent {
    public Card20_109() {
        super(Side.SHADOW, 0, Culture.FALLEN_REALMS, "Discipline", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Keyword.EASTERLING);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Keyword.EASTERLING));
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new CantTakeWoundsModifier(self, new NotCondition(new PhaseCondition(Phase.SKIRMISH)), Keyword.EASTERLING), Phase.REGROUP));
        return action;
    }
}
