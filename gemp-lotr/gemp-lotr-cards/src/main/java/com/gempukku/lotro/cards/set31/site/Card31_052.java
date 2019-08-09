package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
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
        super("Lake-Town", SitesBlock.HOBBIT, 6, 3, Direction.RIGHT);
		addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new TwilightCostModifier(self, Filters.and(Race.ORC,
                new Filter() {
                    @Override
                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                        return game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(self, Phase.SHADOW).getUsedLimit() < 1;
                    }
                }), new PhaseCondition(Phase.SHADOW), -2));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Race.ORC)
                && PlayConditions.isPhase(game, Phase.SHADOW))
            game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(self, Phase.SHADOW).incrementToLimit(1, 1);
        return null;
    }
}