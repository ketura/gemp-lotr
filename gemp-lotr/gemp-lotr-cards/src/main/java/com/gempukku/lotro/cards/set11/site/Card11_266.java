package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Forest. Shadow: Remove a burden to play a Nazgul from your discard pile.
 */
public class Card11_266 extends AbstractShadowsSite {
    public Card11_266() {
        super("Woody-End", 0, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && PlayConditions.canRemoveBurdens(game, self, 1)
                && PlayConditions.canPlayFromDiscard(playerId, game, Race.NAZGUL)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(playerId, self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Race.NAZGUL));
            return Collections.singletonList(action);
        }
        return null;
    }
}
