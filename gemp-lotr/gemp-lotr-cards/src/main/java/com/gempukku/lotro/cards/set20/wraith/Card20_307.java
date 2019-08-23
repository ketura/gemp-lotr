package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * •Ulaire Lemenya, Twilight Spectre
 * Minion • Nazgul
 * 9	2	3
 * Twilight.
 * Regroup: Remove a burden to discard a Free Peoples condition
 * http://lotrtcg.org/coreset/ringwraith/ulairelemenyats(r1).jpg
 */
public class Card20_307 extends AbstractMinion {
    public Card20_307() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, Names.lemenya, "Twilight Spectre", true);
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canRemoveBurdens(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(playerId, self, 1));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
