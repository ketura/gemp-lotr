package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class AddSignetModifier extends AbstractModifier {
    private final Signet _signet;

    public AddSignetModifier(PhysicalCard source, Filterable affectFilter, Signet signet) {
        super(source, "Has signet " + signet, affectFilter, ModifierEffect.SIGNET_MODIFIER);
        _signet = signet;
    }

    @Override
    public boolean hasSignet(DefaultGame game, PhysicalCard physicalCard, Signet signet) {
        return (_signet == signet);
    }
}
