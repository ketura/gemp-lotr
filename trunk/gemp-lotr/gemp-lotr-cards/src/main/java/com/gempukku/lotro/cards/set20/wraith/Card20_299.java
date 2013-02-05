package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.ForEachBurdenEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 2
 * Petrifying Fear
 * Ringwraith	Event • Skirmish
 * Exert a twilight Nazgul to make a companion skirmishing him strength -1 for each burden you can spot.
 */
public class Card20_299 extends AbstractEvent {
    public Card20_299() {
        super(Side.SHADOW, 2, Culture.WRAITH, "Petrifying Fear", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.NAZGUL, Keyword.TWILIGHT);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.NAZGUL, Keyword.TWILIGHT) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                                        new ForEachBurdenEvaluator(), CardType.COMPANION, Filters.inSkirmishAgainst(character)));
                    }
                });
        return action;
    }
}
