package com.gempukku.lotro.cards.set21.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Twilight Cost: 4
 * Type: Site
 * Site: 4
 * Game Text: Underground. Maneuver: Play The One Ring from your draw deck.
 */
public class Card21_52 extends AbstractSite {
    public Card21_52() {
        super("Goblin Town", Block.HOBBIT, 4, 4, Direction.RIGHT);
		addKeyword(Keyword.UNDERGROUND);

    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.Maneuver, self)
				&& PlayConditions.canSpot(game, 1, Filters.name("Bilbo")) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.name("The One Ring")));
            return Collections.singletonList(action);
        }
        return null;
    }
}