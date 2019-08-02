package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.DoesNotAddToArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Aragorn's Bow, Swift and True
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession - Ranged Weapon
 * Card Number: 1R96
 * Game Text: Bearer must be Aragorn.
 * He is an archer.
 * At the beginning of the archery phase, you may exert Aragorn to wound a minion. If you do, Aragorn does not add
 * to the fellowship archery total.
 */
public class Card40_096 extends AbstractAttachableFPPossession {
    public Card40_096() {
        super(1, 0, 0, Culture.GONDOR, PossessionClass.RANGED_WEAPON, "Aragorn's Bow", "Swift and True", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.aragorn;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, Filters.hasAttached(self), Keyword.ARCHER);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.ARCHERY)
        && PlayConditions.canExert(self, game, Filters.aragorn)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.aragorn));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new DoesNotAddToArcheryTotalModifier(self, self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
