package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
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
 * Set: The Short Rest
 * Twilight Cost: 3
 * Type: Site
 * Site: 6
 * Game Text: Sanctuary. The twilight cost of the first Orc played at Lake-Town each turn is -2.
 */
public class Card31_052 extends AbstractSite {
    public Card31_052() {
        super("Lake-Town", Block.HOBBIT, 6, 3, Direction.RIGHT);
		addKeyword(Keyword.RIVER);
    }

	@Override
    public Modifier getAlwaysOnModifier(LotroGame game, final PhysicalCard self) {
        return new TwilightCostModifier(self, Filters.and(Race.ORC,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return modifiersQuerying.getUntilEndOfPhaseLimitCounter(self, Phase.SHADOW).getUsedLimit() < 1;
                    }
                }), new PhaseCondition(Phase.SHADOW), -2);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Race.ORC)
                && PlayConditions.isPhase(game, Phase.SHADOW))
            game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(self, Phase.SHADOW).incrementToLimit(1, 1);
        return null;
    }
}