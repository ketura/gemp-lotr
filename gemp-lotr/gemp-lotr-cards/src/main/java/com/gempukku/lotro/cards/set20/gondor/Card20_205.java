package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;

/**
 * 0
 * Rangers of the North
 * Gondor	Event • Skirmish
 * Make a [Gondor] ranger strength +2 for each site from your adventure deck in the current region.
 */
public class Card20_205 extends AbstractEvent {
    public Card20_205() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Rangers of the North", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(
                        action, self, playerId,
                        new MultiplyEvaluator(2,
                                new CountActiveEvaluator(CardType.SITE, Filters.owner(playerId), Filters.region(GameUtils.getRegion(game)))), Culture.GONDOR, Keyword.RANGER));
        return action;
    }
}
