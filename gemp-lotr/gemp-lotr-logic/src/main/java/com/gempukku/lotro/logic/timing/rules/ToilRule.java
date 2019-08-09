package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignAgainstResult;

import java.util.Collections;
import java.util.List;

public class ToilRule {
    private ModifiersLogic modifiersLogic;

    public ToilRule(ModifiersLogic modifiersLogic) {
        this.modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        modifiersLogic.addAlwaysOnModifier(
                new AbstractModifier(null, "Toil discount", Filters.any, ModifierEffect.POTENTIAL_DISCOUNT_MODIFIER) {
                    @Override
                    public int getPotentialDiscount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard discountCard) {
                        int toilCount = modifiersQuerying.getKeywordCount(gameState, discountCard, Keyword.TOIL);
                        if (toilCount > 0)
                            return toilCount * Filters.countActive(gameState, modifiersQuerying, Filters.owner(discountCard.getOwner()),
                                    discountCard.getBlueprint().getCulture(), Filters.character, Filters.canExert(discountCard));

                        return 0;
                    }
                });
    }
}
