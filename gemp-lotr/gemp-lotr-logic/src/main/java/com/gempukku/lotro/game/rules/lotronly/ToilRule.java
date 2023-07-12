package com.gempukku.lotro.game.rules.lotronly;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.effects.discount.ToilDiscountEffect;
import com.gempukku.lotro.game.modifiers.AbstractModifier;
import com.gempukku.lotro.game.modifiers.ModifierEffect;
import com.gempukku.lotro.game.modifiers.ModifiersLogic;

public class ToilRule {
    private final ModifiersLogic modifiersLogic;

    public ToilRule(ModifiersLogic modifiersLogic) {
        this.modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        modifiersLogic.addAlwaysOnModifier(
                new AbstractModifier(null, "Toil discount", Keyword.TOIL, ModifierEffect.POTENTIAL_DISCOUNT_MODIFIER) {
                    @Override
                    public int getPotentialDiscount(DefaultGame game, PhysicalCard discountCard) {
                        int toilCount = game.getModifiersQuerying().getKeywordCount(game, discountCard, Keyword.TOIL);
                        if (toilCount > 0)
                            return toilCount * Filters.countActive(game, Filters.owner(discountCard.getOwner()),
                                    discountCard.getBlueprint().getCulture(), Filters.character, Filters.canExert(discountCard));

                        return 0;
                    }

                    @Override
                    public void appendPotentialDiscounts(DefaultGame game, CostToEffectAction action, PhysicalCard card) {
                        int toilCount = game.getModifiersQuerying().getKeywordCount(game, card, Keyword.TOIL);
                        if (toilCount > 0)
                            action.appendPotentialDiscount(
                                    new ToilDiscountEffect(action, card, card.getOwner(), card.getBlueprint().getCulture(), toilCount));
                    }
                });
    }
}
