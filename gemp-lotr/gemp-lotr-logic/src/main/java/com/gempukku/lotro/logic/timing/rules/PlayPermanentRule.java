package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
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
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

public class PlayPermanentRule {
    private ModifiersLogic modifiersLogic;

    public PlayPermanentRule(ModifiersLogic modifiersLogic) {
        this.modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        modifiersLogic.addAlwaysOnModifier(
                new AbstractModifier(null, "Play card from hand", Filters.and(Zone.HAND, Filters.not(CardType.EVENT)), ModifierEffect.EXTRA_ACTION_MODIFIER) {
                    @Override
                    public List<? extends Action> getExtraPhaseAction(LotroGame game, final PhysicalCard card) {
                        final LotroCardBlueprint blueprint = card.getBlueprint();

                        final Phase phase = (blueprint.getSide() == Side.FREE_PEOPLE) ? Phase.FELLOWSHIP : Phase.SHADOW;
                        if (PlayConditions.isPhase(game, phase)
                                && PlayUtils.checkPlayRequirements(game, card, Filters.any, 0, 0, false, false)) {
                            return Collections.singletonList(PlayUtils.getPlayCardAction(game, card, 0, Filters.any, false));
                        }
                        return null;
                    }
                });
    }
}
