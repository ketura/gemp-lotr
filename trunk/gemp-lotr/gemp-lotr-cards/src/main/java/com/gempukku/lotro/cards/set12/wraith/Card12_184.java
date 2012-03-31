package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.results.AfterAllSkirmishesResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Possession • Mount
 * Strength: +2
 * Game Text: Bearer must be a Nazgul. If bearer is The Witch-king, after all skirmishes and fierce skirmishes have been
 * resolved, you may exert him twice to make him participate in one additional assignment and skirmish phase.
 */
public class Card12_184 extends AbstractAttachable {
    public Card12_184() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.WRAITH, PossessionClass.MOUNT, "The Witch-king's Beast", "Fell Creature", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.NAZGUL;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.hasAttached(self), 2);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, final EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.AFTER_ALL_SKIRMISHES
                && PlayConditions.canExert(self, game, 2, Filters.witchKing, Filters.hasAttached(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.witchKing, Filters.hasAttached(self)));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new CantBeAssignedToSkirmishModifier(self, Filters.and(CardType.MINION, Filters.not(self.getAttachedTo()))), Phase.ASSIGNMENT));
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            ((AfterAllSkirmishesResult) effectResult).setCreateAnExtraAssignmentAndSkirmishPhases(true);
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
