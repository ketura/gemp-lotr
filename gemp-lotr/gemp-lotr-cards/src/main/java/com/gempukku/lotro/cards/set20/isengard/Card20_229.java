package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;

/**
 * 0
 * Open War
 * Isengard	Event • Skirmish
 * Make an Uruk-hai strength + 1 for each battleground in the current region.
 */
public class Card20_229 extends AbstractEvent {
    public Card20_229() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Open War", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CountActiveEvaluator(CardType.SITE, Keyword.BATTLEGROUND, Filters.region(GameUtils.getRegion(game.getGameState()))), Race.URUK_HAI));
        return action;
    }
}
