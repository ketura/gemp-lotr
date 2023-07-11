package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.PlayUtils;
import com.gempukku.lotro.game.modifiers.AbstractModifier;
import com.gempukku.lotro.game.modifiers.Modifier;
import com.gempukku.lotro.game.modifiers.ModifierEffect;
import com.gempukku.lotro.game.actions.Action;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.List;

public class CanPlayStackedCards implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "filter", "on", "requires");

        final String filter = FieldUtils.getString(object.get("filter"), "filter");
        final String onFilter = FieldUtils.getString(object.get("on"), "on");
        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("requires"), "requires");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
        final FilterableSource onFilterableSource = environment.getFilterFactory().generateFilter(onFilter, environment);
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        return new ModifierSource() {
            @Override
            public Modifier getModifier(ActionContext actionContext) {
                return new AbstractModifier(actionContext.getSource(), null,
                        Filters.and(filterableSource.getFilterable(actionContext), Filters.stackedOn(onFilterableSource.getFilterable(actionContext))),
                        new RequirementCondition(requirements, actionContext), ModifierEffect.EXTRA_ACTION_MODIFIER) {
                    @Override
                    public List<? extends Action> getExtraPhaseActionFromStacked(DefaultGame game, PhysicalCard card) {
                        if (PlayUtils.checkPlayRequirements(game, card, Filters.any, 0, 0, false, false, false))
                            return Collections.singletonList(
                                    PlayUtils.getPlayCardAction(game, card, 0, Filters.any, false));
                        return null;
                    }
                };
            }
        };
    }
}
