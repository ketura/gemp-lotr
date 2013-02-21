package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CommonEffects;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 2
 * Outer Wall
 * Rohan	Possession • Support Area
 * Fortification.
 * To spot two Valiant men.
 * Each minion skirmishing an unbound companion loses fierce and cannot gain fierce until the regroup phase.
 * Discard this condition start of the regroup phase.
 */
public class Card20_336 extends AbstractPermanent {
    public Card20_336() {
        super(Side.FREE_PEOPLE, 2, CardType.POSSESSION, Culture.ROHAN, Zone.SUPPORT, "Outer Wall");
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Race.MAN, Keyword.VALIANT);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        Set<Integer> minionsMarked = (Set<Integer>) self.getWhileInZoneData();
        if (minionsMarked == null) {
            minionsMarked = new HashSet<Integer>();
            self.setWhileInZoneData(minionsMarked);
        }

        Set<PhysicalCard> toLose = new HashSet<PhysicalCard>();
        final Set<Integer> toLoseInts = new HashSet<Integer>();
        for (PhysicalCard minion : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmishAgainst(Filters.unboundCompanion))) {
            if (!minionsMarked.contains(minion.getCardId())) {
                toLose.add(minion);
                toLoseInts.add(minion.getCardId());
            }
        }

        if (toLose.size() > 0) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            final Set<Integer> finalMinionsMarked = minionsMarked;
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            finalMinionsMarked.addAll(toLoseInts);
                        }
                    });
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new RemoveKeywordModifier(self, Filters.in(toLose), Keyword.FIERCE), Phase.REGROUP));
            return Collections.singletonList(action);
        }

        return CommonEffects.getSelfDiscardAtStartOfRegroup(game, effectResult, self);
    }
}
