package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.IncrementPhaseLimitEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.ExertResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Possession • Ranged Weapon
 * Game Text: Bearer must be an Elf. Bearer is an archer. If bearer is Legolas, each time you exert him to play
 * an [ELVEN] condition or [ELVEN] event, you may heal him (limit once per phase).
 */
public class Card11_023 extends AbstractAttachableFPPossession {
    public Card11_023() {
        super(2, 0, 0, Culture.ELVEN, PossessionClass.RANGED_WEAPON, "Legolas' Bow", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ELF;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.ARCHER));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachExerted(game, effectResult, self.getAttachedTo())
                && self.getAttachedTo().getBlueprint().getTitle().equals("Legolas")
                && PlayConditions.checkPhaseLimit(game, self, 1)) {
            ExertResult exertResult = (ExertResult) effectResult;
            if (exertResult.getAction() != null && exertResult.getAction().getType() == Action.Type.PLAY_CARD) {
                PhysicalCard playedCard = exertResult.getAction().getActionSource();
                if (playedCard != null && Filters.and(Culture.ELVEN, Filters.or(CardType.CONDITION, CardType.EVENT)).accepts(game, playedCard)) {
                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    action.appendCost(
                            new IncrementPhaseLimitEffect(self, 1));
                    action.appendEffect(
                            new HealCharactersEffect(self, self.getOwner(), self.getAttachedTo()));
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }
}
