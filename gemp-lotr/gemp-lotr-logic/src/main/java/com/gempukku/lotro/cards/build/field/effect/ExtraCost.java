package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.BuiltLotroCardBlueprint;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.game.ExtraPlayCost;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.modifiers.Condition;
import org.json.simple.JSONObject;

public class ExtraCost implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "cost");

        final EffectAppender costAppender = environment.getEffectAppenderFactory().getEffectAppender((JSONObject) value.get("cost"), environment);

        blueprint.appendExtraPlayCost(
                (game, card) -> new ExtraPlayCost() {
                    @Override
                    public void appendExtraCosts(LotroGame game, CostToEffectAction action, PhysicalCard card) {
                        DefaultActionContext actionContext = new DefaultActionContext(card.getOwner(), game, card, null, null);
                        costAppender.appendEffect(true, action, actionContext);
                    }

                    @Override
                    public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card) {
                        DefaultActionContext actionContext = new DefaultActionContext(card.getOwner(), game, card, null, null);
                        return costAppender.isPlayableInFull(actionContext);
                    }

                    @Override
                    public Condition getCondition() {
                        return null;
                    }
                });
    }
}
