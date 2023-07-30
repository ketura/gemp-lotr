package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;

public class AddSignetModifier extends AbstractModifier {
    private final Signet _signet;

    public AddSignetModifier(LotroPhysicalCard source, Filterable affectFilter, Signet signet) {
        super(source, "Has signet " + signet, affectFilter, ModifierEffect.SIGNET_MODIFIER);
        _signet = signet;
    }

    @Override
    public boolean hasSignet(DefaultGame game, LotroPhysicalCard physicalCard, Signet signet) {
        return (_signet == signet);
    }
}
