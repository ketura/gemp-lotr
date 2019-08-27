package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class RequirementCondition implements Condition {
    private Requirement[] requirements;
    private String playerId;
    private PhysicalCard self;
    private EffectResult effectResult;
    private Effect effect;

    public RequirementCondition(Requirement[] requirements, String playerId, PhysicalCard self, EffectResult effectResult, Effect effect) {
        this.requirements = requirements;
        this.playerId = playerId;
        this.self = self;
        this.effectResult = effectResult;
        this.effect = effect;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        for (Requirement requirement : requirements) {
            if (!requirement.accepts(playerId, game, self, effectResult, effect))
                return false;
        }

        return true;
    }
}
