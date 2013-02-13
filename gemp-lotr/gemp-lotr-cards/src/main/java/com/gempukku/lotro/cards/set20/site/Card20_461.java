package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Gates of Minas Morgul
 * 8	8
 * Shadow: Exert a Nazgul to play a Nazgul; it's twilight cost is -2.
 */
public class Card20_461 extends AbstractSite {
    public Card20_461() {
        super("Gates of Minas Morgul", Block.SECOND_ED, 8, 8, null);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && PlayConditions.canExert(self, game, Race.NAZGUL)
                && PlayConditions.canPlayFromHand(playerId, game, -2, Race.NAZGUL)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.NAZGUL));
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, -2, Race.NAZGUL));
            return Collections.singletonList(action);
        }
        return null;
    }
}
