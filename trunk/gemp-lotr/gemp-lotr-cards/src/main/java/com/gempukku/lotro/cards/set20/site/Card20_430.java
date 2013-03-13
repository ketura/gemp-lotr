package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Galadriel's Grove
 * 3	0
 * Forest. Sanctuary.
 * Fellowship: Exert an Elf to play Galadriel from your draw deck.
 */
public class Card20_430 extends AbstractSite {
    public Card20_430() {
        super("Galadriel's Grove", Block.SECOND_ED, 3, 0, null);
        addKeyword(Keyword.FOREST);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && GameUtils.isFP(game, playerId)
            && PlayConditions.canExert(self, game, Race.ELF)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId,1, 1, Race.ELF));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.galadriel));
            return Collections.singletonList(action);
        }
        return null;
    }
}
