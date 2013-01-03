package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * 2
 * • Crebain Spies
 * Dunland	Condition • Support Area
 * To play, spot Saruman or a [Dunland] Man.
 * While you can spot Saruman or a [Dunland] Man, the shadow number of each site is +X, where X is the fellowship’s
 * current region number.
 */
public class Card20_037 extends AbstractPermanent {
    public Card20_037() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.DUNLAND, Zone.SUPPORT, "Crebain Spies", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.or(Filters.saruman, Filters.and(Culture.DUNLAND, Race.MAN)));
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new TwilightCostModifier(self, CardType.SITE, new SpotCondition(Filters.or(Filters.saruman, Filters.and(Culture.DUNLAND, Race.MAN))),
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                        return GameUtils.getRegion(gameState);
                    }
                });
    }
}
