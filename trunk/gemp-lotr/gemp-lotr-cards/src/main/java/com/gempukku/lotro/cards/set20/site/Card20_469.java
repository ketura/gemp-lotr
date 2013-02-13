package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Tower of Barad-dur
 * 9	9
 * Shadow: Play The Great Eye from your draw deck or discard pile; its twilight cost is -2.
 */
public class Card20_469 extends AbstractSite {
    public Card20_469() {
        super("Tower of Barad-dur", Block.SECOND_ED, 9, 9, null);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, -2, Filters.name("The Great Eye")) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play The Great Eye from your draw deck";
                        }
                    });
            possibleEffects.add(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, -2, Filters.name("The Great Eye")) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play The Great Eye from your discard";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
