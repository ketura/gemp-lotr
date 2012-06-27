package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 11
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Skirmish: Spot a Free Peoples player’s site on the adventure path and exert this minion twice
 * to exchange the spotted site with a site you control. You now control the spotted site.
 */
public class Card15_174 extends AbstractMinion {
    public Card15_174() {
        super(5, 11, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Cavern Striker");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSpot(game, CardType.SITE, Zone.ADVENTURE_PATH, Filters.owner(game.getGameState().getCurrentPlayerId()))
                && PlayConditions.canSelfExert(self, 2, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose site", CardType.SITE, Zone.ADVENTURE_PATH, Filters.owner(game.getGameState().getCurrentPlayerId())) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard siteOnPath) {
                            action.appendEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose your controller site", Filters.siteControlled(playerId)) {
                                        @Override
                                        protected void cardSelected(LotroGame game, final PhysicalCard controlledSite) {
                                            action.appendEffect(
                                                    new UnrespondableEffect() {
                                                        @Override
                                                        protected void doPlayEffect(LotroGame game) {
                                                            int onPathNumber = siteOnPath.getSiteNumber();
                                                            int controlledNumber = controlledSite.getSiteNumber();
                                                            siteOnPath.setSiteNumber(controlledNumber);
                                                            controlledSite.setSiteNumber(onPathNumber);
                                                            game.getGameState().loseControlOfCard(controlledSite, Zone.ADVENTURE_PATH);
                                                            game.getGameState().takeControlOfCard(playerId, game, siteOnPath, Zone.SUPPORT);
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new SelfExertEffect(action, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
