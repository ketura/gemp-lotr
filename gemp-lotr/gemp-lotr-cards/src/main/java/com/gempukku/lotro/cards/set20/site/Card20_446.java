package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Great Feasting Hall
 * 6	3
 * Sanctuary.
 * Fellowship: Exert two unbound Hobbits to heal an unbound companion twice.
 */
public class Card20_446 extends AbstractSite {
    public Card20_446() {
        super("Great Feasting Hall", Block.SECOND_ED, 6, 3, null);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, 1, 2, Filters.unboundCompanion, Race.HOBBIT)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, 1, Filters.unboundCompanion, Race.HOBBIT));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, 2, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
