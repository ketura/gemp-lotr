package com.gempukku.lotro.cards.set3.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Twilight Cost: 9
 * Type: Site
 * Site: 9
 * Game Text: Maneuver: Spot 4 companions and exert your [ISENGARD] Orc to make the Free Peoples player wound
 * a companion.
 */
public class Card3_120 extends AbstractSite {
    public Card3_120() {
        // TODO Check direction of the site
        super("Wastes of Emyn Muil", Block.FELLOWSHIP, 9, 9, null);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)) >= 4
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.owner(playerId), Filters.culture(Culture.ISENGARD), Filters.race(Race.ORC))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.owner(playerId), Filters.culture(Culture.ISENGARD), Filters.race(Race.ORC)));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, Filters.type(CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
