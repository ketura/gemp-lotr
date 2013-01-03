package com.gempukku.lotro.cards.set15.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Set: The Hunters
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Underground. The twilight cost of the first hunter minion played each shadow phase is -2.
 */
public class Card15_188 extends AbstractNewSite {
    public Card15_188() {
        super("Breeding Pit of Isengard", 0, Direction.LEFT);
        addKeyword(Keyword.UNDERGROUND);
    }


    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new TwilightCostModifier(self,
                Filters.and(
                        CardType.MINION,
                        Keyword.HUNTER,
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return modifiersQuerying.getUntilEndOfPhaseLimitCounter(self, Phase.SHADOW).getUsedLimit() < 1;
                            }
                        }), new PhaseCondition(Phase.SHADOW), -2);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.and(CardType.MINION, Keyword.HUNTER))
                && PlayConditions.isPhase(game, Phase.SHADOW))
            game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(self, Phase.SHADOW).incrementToLimit(1, 1);
        return null;
    }
}
