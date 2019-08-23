package com.gempukku.lotro.cards.set30.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Twilight Cost: 4
 * Type: Site
 * Site: 4
 * Game Text: Underground. Maneuver: Play The One Ring from your draw deck.
 */
public class Card30_052 extends AbstractSite {
    public Card30_052() {
        super("Goblin Town", SitesBlock.HOBBIT, 4, 4, Direction.RIGHT);
		addKeyword(Keyword.UNDERGROUND);

    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.MANEUVER, self)
				&& PlayConditions.canSpot(game, 1, Filters.name("Bilbo"))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.name("The One Ring")));
            return Collections.singletonList(action);
        }
        return null;
    }
}