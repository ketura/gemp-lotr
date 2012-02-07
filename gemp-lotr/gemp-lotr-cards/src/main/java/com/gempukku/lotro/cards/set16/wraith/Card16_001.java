package com.gempukku.lotro.cards.set16.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.cards.modifiers.conditions.PhaseCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Wraith Collection
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Minion • Wraith
 * Strength: 11
 * Vitality: 4
 * Site: 2
 * Game Text: Enduring. Each [WRAITH] Wraith cannot take wounds (except during skirmishes).
 * Shadow: Remove a burden to play a [WRAITH] Wraith. That minion's twilight cost is -2.
 */
public class Card16_001 extends AbstractMinion {
    public Card16_001() {
        super(5, 11, 4, 2, Race.WRAITH, Culture.WRAITH, "Barrow-wight Stalker", true);
        addKeyword(Keyword.ENDURING);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantTakeWoundsModifier(self, new NotCondition(new PhaseCondition(Phase.SKIRMISH)), Filters.and(Culture.WRAITH, Race.WRAITH));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canRemoveBurdens(game, self, 1)
                && PlayConditions.canPlayFromHand(playerId, game, -2, Culture.WRAITH, Race.WRAITH)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(playerId, self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, -2, Culture.WRAITH, Race.WRAITH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
