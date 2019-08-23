package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Valley of Imladris
 * 1	0
 * Fellowship: Exert an Elf to play Elrond from your draw deck.
 */
public class Card20_421 extends AbstractSite {
    public Card20_421() {
        super("Valley of Imladris", SitesBlock.SECOND_ED, 1, 0, null);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && GameUtils.isFP(game, playerId)
                && PlayConditions.canExert(self, game, Race.ELF)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.name("Elrond")));
            return Collections.singletonList(action);
        }
        return null;
    }
}
