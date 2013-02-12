package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CommonEffects;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * 2
 * •Gates of the Hornburg
 * Rohan	Possession • Support Area
 * Fortification.
 * To play spot 2 [Rohan] Men.
 * Each minion skirmishing an unbound companion is strength -2.
 * Discard this condition at the start of the regroup phase.
 */
public class Card20_328 extends AbstractPermanent {
    public Card20_328() {
        super(Side.FREE_PEOPLE, 2, CardType.POSSESSION, Culture.ROHAN, Zone.SUPPORT, "Gates of the Hornburg", null, true);
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Culture.ROHAN, Race.MAN);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Filters.unboundCompanion)), -2);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return CommonEffects.getSelfDiscardAtStartOfRegroup(game, effectResult, self);
    }
}
