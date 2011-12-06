package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class IsAdditionalCardTypeModifier extends AbstractModifier {
    private CardType _cardType;

    public IsAdditionalCardTypeModifier(PhysicalCard source, Filterable affectFilter, CardType cardType) {
        this(source, affectFilter, null, cardType);
    }

    public IsAdditionalCardTypeModifier(PhysicalCard source, Filterable affectFilter, Condition condition, CardType cardType) {
        super(source, "Has additional card type - " + cardType.toString(), affectFilter, condition, ModifierEffect.ADDITIONAL_CARD_TYPE);
        _cardType = cardType;
    }

    @Override
    public boolean isAdditionalCardTypeModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, CardType cardType) {
        return cardType == _cardType;
    }
}
