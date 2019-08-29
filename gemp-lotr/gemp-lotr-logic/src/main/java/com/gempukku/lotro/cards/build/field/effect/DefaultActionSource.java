package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.ActionSource;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

public class DefaultActionSource implements ActionSource {
    private List<Requirement> requirements = new LinkedList<>();

    private List<EffectAppender> costs = new LinkedList<>();
    private List<EffectAppender> effects = new LinkedList<>();

    public void addPlayRequirement(Requirement requirement) {
        this.requirements.add(requirement);
    }

    public void addCost(EffectAppender effectAppender) {
        costs.add(effectAppender);
    }

    public void addEffect(EffectAppender effectAppender) {
        effects.add(effectAppender);
    }

    @Override
    public boolean isValid(String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        for (Requirement requirement : requirements) {
            if (!requirement.accepts(null, playerId, game, self, effectResult, effect))
                return false;
        }
        return true;
    }

    @Override
    public void createAction(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        for (EffectAppender cost : costs)
            cost.appendCost(action, playerId, game, self, effectResult, effect);

        for (EffectAppender actionEffect : effects)
            actionEffect.appendEffect(action, playerId, game, self, effectResult, effect);
    }
}
