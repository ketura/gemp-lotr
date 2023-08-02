package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.ActionSource;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.effect.appender.EffectAppender;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;

import java.util.LinkedList;
import java.util.List;

public class DefaultActionSource implements ActionSource {
    private final List<Requirement> requirements = new LinkedList<>();

    private final List<EffectAppender> costs = new LinkedList<>();
    private final List<EffectAppender> effects = new LinkedList<>();

    private boolean requiresRanger;
    private String text;

    public void setRequiresRanger(boolean requiresRanger) {
        this.requiresRanger = requiresRanger;
    }

    public void setText(String text) {
        this.text = text;
    }

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
    public boolean requiresRanger() {
        return requiresRanger;
    }

    @Override
    public boolean isValid(DefaultActionContext<DefaultGame> actionContext) {
        for (Requirement requirement : requirements) {
            if (!requirement.accepts(actionContext))
                return false;
        }
        return true;
    }

    @Override
    public void createAction(CostToEffectAction action, DefaultActionContext actionContext) {
        if (text != null)
            action.setText(GameUtils.SubstituteText(text, actionContext));

        for (EffectAppender cost : costs)
            cost.appendEffect(true, action, actionContext);

        for (EffectAppender actionEffect : effects)
            actionEffect.appendEffect(false, action, actionContext);
    }
}
