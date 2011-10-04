package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PlaySiteEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Minion • Nazgul
 * Strength: 10
 * Vitality: 3
 * Site: 2
 * Game Text: Fierce. Shadow: Exert Ulaire Nelya and spot an opponent's site to replace it with your site of the same
 * number.
 */
public class Card1_233 extends AbstractMinion {
    public Card1_233() {
        super(5, 10, 3, 2, Race.NAZGUL, Culture.WRAITH, "Ulaire Nelya", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.SITE), Filters.not(Filters.owner(playerId)))) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.SHADOW);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(playerId, "Choose opponent's site", Filters.type(CardType.SITE), Filters.not(Filters.owner(playerId))) {
                        @Override
                        protected void cardSelected(PhysicalCard site) {
                            action.appendEffect(new PlaySiteEffect(playerId, site.getBlueprint().getSiteNumber()));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
