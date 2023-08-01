package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import com.gempukku.lotro.modifiers.condition.Condition;

public class OverwhelmedByMultiplierModifier extends AbstractModifier {
    private final int _multiplier;

    public OverwhelmedByMultiplierModifier(LotroPhysicalCard source, Filterable affectFilter, int multiplier) {
        this(source, affectFilter, null, multiplier);
    }

    public OverwhelmedByMultiplierModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition, int multiplier) {
        super(source, "Cannot be overwhelmed unless his strength is *" + multiplier, affectFilter, condition, ModifierEffect.OVERWHELM_MODIFIER);
        _multiplier = multiplier;
    }

    @Override
    public int getOverwhelmMultiplier(DefaultGame game, LotroPhysicalCard physicalCard) {
        return _multiplier;
    }
}
