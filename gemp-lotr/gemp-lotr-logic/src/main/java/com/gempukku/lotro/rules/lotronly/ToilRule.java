package com.gempukku.lotro.rules.lotronly;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.effects.discount.ToilDiscountEffect;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import com.gempukku.lotro.modifiers.ModifiersLogic;

public class ToilRule {
    private final ModifiersLogic modifiersLogic;

    public ToilRule(ModifiersLogic modifiersLogic) {
        this.modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        modifiersLogic.addAlwaysOnModifier(
                new AbstractModifier(null, "Toil discount", Keyword.TOIL, ModifierEffect.POTENTIAL_DISCOUNT_MODIFIER) {
                    @Override
                    public int getPotentialDiscount(DefaultGame game, LotroPhysicalCard discountCard) {
                        int toilCount = game.getModifiersQuerying().getKeywordCount(game, discountCard, Keyword.TOIL);
                        if (toilCount > 0)
                            return toilCount * Filters.countActive(game, Filters.owner(discountCard.getOwner()),
                                    discountCard.getBlueprint().getCulture(), Filters.character, Filters.canExert(discountCard));

                        return 0;
                    }

                    @Override
                    public void appendPotentialDiscounts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card) {
                        int toilCount = game.getModifiersQuerying().getKeywordCount(game, card, Keyword.TOIL);
                        if (toilCount > 0)
                            action.appendPotentialDiscount(
                                    new ToilDiscountEffect(action, card, card.getOwner(), card.getBlueprint().getCulture(), toilCount));
                    }
                });
    }
}
