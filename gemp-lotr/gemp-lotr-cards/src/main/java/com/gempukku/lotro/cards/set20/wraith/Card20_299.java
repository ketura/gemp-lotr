package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ForEachBurdenEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.NAZGUL, Keyword.TWILIGHT);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.NAZGUL, Keyword.TWILIGHT) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                                        new MultiplyEvaluator(-1, new ForEachBurdenEvaluator()), CardType.COMPANION, Filters.inSkirmishAgainst(character)));
                    }
                });
        return action;
    }
}
