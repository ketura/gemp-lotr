package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class AddSignetModifier extends AbstractModifier {
    private Signet _signet;

    public AddSignetModifier(PhysicalCard source, Filterable affectFilter, Signet signet) {
        super(source, "Has signet " + signet, affectFilter, ModifierEffect.SIGNET_MODIFIER);
        _signet = signet;
    }

    @Override
    public boolean hasSignet(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Signet signet) {
        return (_signet == signet);
    }
}
