package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: Orc Savage
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Minion - Orc
 * Strength: 7
 * Vitality: 2
 * Home: 6
 * Card Number: 1C232
 * Game Text: Tracker. Regroup: Spot another [SAURON] Orc and discard this minion to make the Free Peoples player exert a companion.
 */
public class Card40_232 extends AbstractMinion {
    public Card40_232() {
        super(2, 7, 2, 6, Race.ORC, Culture.SAURON, "Orc Savage");
        addKeyword(Keyword.TRACKER);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canSpot(game, Culture.SAURON, Race.ORC, Filters.not(self))
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, GameUtils.getFreePeoplePlayer(game), 1, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
