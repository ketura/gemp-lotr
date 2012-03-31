package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
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
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ELF;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.ARCHER));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachExerted(game, effectResult, self.getAttachedTo())
                && self.getAttachedTo().getBlueprint().getName().equals("Legolas")) {
            ExertResult exertResult = (ExertResult) effectResult;
            if (exertResult.getAction() != null && exertResult.getAction().getType() == Action.Type.PLAY_CARD) {
                PhysicalCard playedCard = exertResult.getAction().getActionSource();
                if (playedCard != null && Filters.and(Culture.ELVEN, Filters.or(CardType.CONDITION, CardType.EVENT)).accepts(game.getGameState(), game.getModifiersQuerying(), playedCard)) {
                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    action.appendEffect(
                            new CheckPhaseLimitEffect(action, self, 1,
                                    new HealCharactersEffect(self, self.getAttachedTo())));
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }
}
