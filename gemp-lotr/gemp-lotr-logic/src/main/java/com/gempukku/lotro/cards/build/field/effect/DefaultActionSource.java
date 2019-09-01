package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.ActionSource;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.logic.actions.CostToEffectAction;

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
    public boolean isValid(ActionContext actionContext) {
        for (Requirement requirement : requirements) {
            if (!requirement.accepts(actionContext))
                return false;
        }
        return true;
    }

    @Override
    public void createAction(CostToEffectAction action, ActionContext actionContext) {
        for (EffectAppender cost : costs)
            cost.appendEffect(true, action, actionContext);

        for (EffectAppender actionEffect : effects)
            actionEffect.appendEffect(false, action, actionContext);
    }
}
