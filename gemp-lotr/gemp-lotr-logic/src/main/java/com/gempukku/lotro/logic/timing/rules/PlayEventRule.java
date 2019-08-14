package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayEventRule {
    private ModifiersLogic modifiersLogic;
    private Map<Phase, Keyword> phaseKeywordMap;

    public PlayEventRule(ModifiersLogic modifiersLogic) {
        this.modifiersLogic = modifiersLogic;

        phaseKeywordMap = new HashMap<>();
        phaseKeywordMap.put(Phase.FELLOWSHIP, Keyword.FELLOWSHIP);
        phaseKeywordMap.put(Phase.SHADOW, Keyword.SHADOW);
        phaseKeywordMap.put(Phase.MANEUVER, Keyword.MANEUVER);
        phaseKeywordMap.put(Phase.ARCHERY, Keyword.ARCHERY);
        phaseKeywordMap.put(Phase.ASSIGNMENT, Keyword.ASSIGNMENT);
        phaseKeywordMap.put(Phase.SKIRMISH, Keyword.SKIRMISH);
        phaseKeywordMap.put(Phase.REGROUP, Keyword.REGROUP);
    }

    public void applyRule() {
        modifiersLogic.addAlwaysOnModifier(
                new AbstractModifier(null, "Play card from hand", Filters.and(Zone.HAND, CardType.EVENT), ModifierEffect.EXTRA_ACTION_MODIFIER) {
                    @Override
                    public List<? extends Action> getExtraPhaseAction(LotroGame game, final PhysicalCard card) {
                        final LotroCardBlueprint blueprint = card.getBlueprint();

                        if (checkIfCorrectPhase(game, blueprint)
                                && PlayUtils.checkPlayRequirements(game, card, Filters.any, 0, 0, false, false)) {
                            return Collections.singletonList(PlayUtils.getPlayCardAction(game, card, 0, Filters.any, false));
                        }
                        return null;
                    }
                });
    }

    private boolean checkIfCorrectPhase(LotroGame game, LotroCardBlueprint blueprint) {
        final Phase currentPhase = game.getGameState().getCurrentPhase();
        final Keyword keyword = phaseKeywordMap.get(currentPhase);
        if (keyword != null && blueprint.hasKeyword(keyword))
            return true;
        return false;
    }
}
