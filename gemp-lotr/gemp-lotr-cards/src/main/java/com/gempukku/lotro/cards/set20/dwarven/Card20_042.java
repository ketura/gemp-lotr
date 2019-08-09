package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * 0
 * Brothers in Arms
 * Dwarven	Event • Skirmish
 * Exert a Dwarf to make another Dwarf strength +X where X is the number of [Dwarven] cards stacked on [Dwarven] conditions.
 */
public class Card20_042 extends AbstractEvent {
    public Card20_042() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Brothers in Arms", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.DWARF);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                                        new Evaluator() {
                                            @Override
                                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                                int count = 0;
                                                for (PhysicalCard dwarvenCondition : Filters.filterActive(game, Culture.DWARVEN, CardType.CONDITION)) {
                                                    count += Filters.filter(game.getGameState().getStackedCards(dwarvenCondition), game, Culture.DWARVEN).size();
                                                }
                                                return count;
                                            }
                                        }, Race.DWARF, Filters.not(character)));
                    }
                });
        return action;
    }
}
