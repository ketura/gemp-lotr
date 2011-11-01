package com.gempukku.lotro.cards.set5.gollum;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PlaySiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays on Smeagol. Regroup: Exert Smeagol or Gollum to play the fellowship's next site (replacing
 * opponent's site if necessary).
 */
public class Card5_023 extends AbstractAttachable {
    public Card5_023() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.GOLLUM, null, "Follow Smeagol");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.smeagol;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, Filters.gollumOrSmeagol)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gollumOrSmeagol));
            action.appendEffect(
                    new PlaySiteEffect(playerId, null, game.getGameState().getCurrentSiteNumber() + 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
