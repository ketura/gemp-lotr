package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Maneuver: If the Free People’s player has at least 1 site in his or her adventure deck, exert 2 [MEN] Men
 * to randomly choose a site from that deck. Replace the fellowship’s current site with that site.
 */
public class Card15_092 extends AbstractMinion {
    public Card15_092() {
        super(3, 8, 2, 4, Race.MAN, Culture.MEN, "Savage Southron");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && game.getGameState().getAdventureDeck(game.getGameState().getCurrentPlayerId()).size() > 0
                && PlayConditions.canExert(self, game, 1, 2, Culture.MEN, Race.MAN)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Culture.MEN, Race.MAN));
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            final List<PhysicalCard> randomCards = GameUtils.getRandomCards(game.getGameState().getAdventureDeck(game.getGameState().getCurrentPlayerId()), 1);
                            action.appendEffect(
                                    new PlaySiteEffect(action, game.getGameState().getCurrentPlayerId(), null, game.getGameState().getCurrentSiteNumber(), Filters.in(randomCards)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
