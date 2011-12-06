package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Marsh. Shadow: If the total number of minions and twilight tokens is 3 or fewer, play a minion at twilight
 * cost -3.
 */
public class Card11_238 extends AbstractNewSite {
    public Card11_238() {
        super("Expanding Marshland", 1, Direction.RIGHT);
        addKeyword(Keyword.MARSH);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && (Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.MINION) + game.getGameState().getTwilightPool()) <= 3
                && PlayConditions.canPlayFromHand(playerId, game, -3, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, -3, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
