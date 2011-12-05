package com.gempukku.lotro.cards.set11.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.cards.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Possession • Ranged Weapon
 * Strength: +1
 * Game Text: Bearer must be a [ROHAN] Man. Archery: Discard this possession to make the fellowship archery total +1
 * (or +2 if the fellowship is at a plains site).
 */
public class Card11_151 extends AbstractAttachableFPPossession {
    public Card11_151() {
        super(1, 1, 0, Culture.ROHAN, PossessionClass.RANGED_WEAPON, "Riddermark Javelin");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, null, new ConditionEvaluator(1, 2, new LocationCondition(Keyword.PLAINS))), Phase.ARCHERY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
