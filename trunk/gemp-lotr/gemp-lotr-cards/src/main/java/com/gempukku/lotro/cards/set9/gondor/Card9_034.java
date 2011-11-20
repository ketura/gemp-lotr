package com.gempukku.lotro.cards.set9.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Artifact • Hand Weapon
 * Vitality: +1
 * Game Text: Bearer must be a [GONDOR] Man. If bearer is Elendil or Isildur, he is strength +1 for each [GONDOR]
 * artifact you can spot (limit +6). Response: If bearer is about to take a wound, exert 2 [GONDOR] Men to prevent that.
 */
public class Card9_034 extends AbstractAttachableFPPossession {
    public Card9_034() {
        super(2, 0, 1, Culture.GONDOR, CardType.ARTIFACT, PossessionClass.HAND_WEAPON, "Narsil", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Filters.hasAttached(self), Filters.or(Filters.name("Elendil"), Filters.name("Isildur"))), null, new CountSpottableEvaluator(6, Culture.GONDOR, CardType.ARTIFACT)));
    }

    @Override
    public List<? extends Action> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, self.getAttachedTo())
                && PlayConditions.canExert(self, game, 1, 2, Culture.GONDOR, Race.MAN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Culture.GONDOR, Race.MAN));
            action.appendEffect(
                    new PreventCardEffect((WoundCharactersEffect) effect, self.getAttachedTo()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
