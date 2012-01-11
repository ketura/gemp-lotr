package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 2
 * Site: 4
 * Game Text: Regroup: Discard this minion from play (or exert this minion if the fellowship’s next site is an
 * underground site) and spot another [ORC] minion to return that minion to your hand.
 */
public class Card13_114 extends AbstractMinion {
    public Card13_114() {
        super(2, 7, 2, 4, Race.ORC, Culture.ORC, "Orc Plains Runner");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.ORC, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new SelfDiscardEffect(self));
            PhysicalCard nextSite = game.getGameState().getSite(game.getGameState().getCurrentSiteNumber() + 1);
            if (nextSite != null
                    && Filters.and(Keyword.UNDERGROUND).accepts(game.getGameState(), game.getModifiersQuerying(), nextSite)
                    && PlayConditions.canSelfExert(self, game))
                possibleCosts.add(
                        new SelfExertEffect(self));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendCost(
                    new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, Filters.not(self), Culture.ORC, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
