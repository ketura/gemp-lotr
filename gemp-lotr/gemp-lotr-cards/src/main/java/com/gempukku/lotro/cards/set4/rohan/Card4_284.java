package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Possession • Armor
 * Game Text: Bearer must be a [ROHAN] Man. Bearer takes no more than 1 wound during each skirmish phase. If bearer
 * is Theoden, he may not take wounds except during a skirmish involving him.
 */
public class Card4_284 extends AbstractAttachableFPPossession {
    public Card4_284() {
        super(2, 0, 0, Culture.ROHAN, PossessionClass.ARMOR, "King's Mail", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new CantTakeWoundsModifier(self,
                        Filters.and(
                                Filters.hasAttached(self),
                                Filters.name("Theoden"),
                                Filters.not(Filters.inSkirmish)
                        )));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.isWounded(effectResult, self.getAttachedTo())
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new CantTakeWoundsModifier(self, Filters.sameCard(self.getAttachedTo())), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
