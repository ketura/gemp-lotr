package com.gempukku.lotro.cards.set31.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Possession • Support Area
 * Game Text: Fortification. To play, exert a [DWARVEN] companion. If the fellowship moves during the regroup phase,
 * discard this possession. At sites 7 and 8, each minion skirmishing a [DWARVEN] companion is strength -3.
 */
public class Card31_004 extends AbstractPermanent {
    public Card31_004() {
        super(Side.FREE_PEOPLE, 2, CardType.POSSESSION, Culture.DWARVEN, "Great Barricade", null, true);
		addKeyword(Keyword.FORTIFICATION);
	}

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Culture.DWARVEN, CardType.COMPANION));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new StrengthModifier(self,
                        Filters.and(
                                CardType.MINION,
                                Filters.inSkirmishAgainst(
                                        Filters.and(
                                                CardType.COMPANION,
                                                Culture.DWARVEN
                                        )
                                )
                        ),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return (game.getGameState().getCurrentSiteNumber() == 7 || game.getGameState().getCurrentSiteNumber() == 8)
										&& game.getGameState().getCurrentSiteBlock() == SitesBlock.HOBBIT;
                            }
                        }, -3));
    }

	
    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)
				&& PlayConditions.isPhase(game, Phase.REGROUP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
