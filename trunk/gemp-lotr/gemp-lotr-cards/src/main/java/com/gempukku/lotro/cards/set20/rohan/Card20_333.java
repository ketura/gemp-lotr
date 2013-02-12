package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CommonEffects;
import com.gempukku.lotro.cards.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * 3
 * Hornburg Parapet
 * Rohan	Possession • Support Area
 * Fortification.
 * To play exert 2 [Rohan] Men.
 * Skip the archery phase.
 * Discard this condition at the start of the regroup phase.
 */
public class Card20_333 extends AbstractPermanent {
    public Card20_333() {
        super(Side.FREE_PEOPLE, 3, CardType.POSSESSION, Culture.ROHAN, Zone.SUPPORT, "Hornburg Parapet");
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, 1, 2, Culture.ROHAN, Race.MAN);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new ShouldSkipPhaseModifier(self, Phase.ARCHERY);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return CommonEffects.getSelfDiscardAtStartOfRegroup(game, effectResult, self);
    }
}
