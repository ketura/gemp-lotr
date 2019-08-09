package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
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
 * Tower of Orthanc
 * 5	7
 * Battleground.
 * Play Saruman from your hand, draw deck, or discard pile; his twilight cost is -2.
 */
public class Card20_444 extends AbstractSite {
    public Card20_444() {
        super("Tower of Orthanc", Block.SECOND_ED, 5, 7, null);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, -2, Filters.saruman) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play Saruman from your hand";
                        }
                    });
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, -2, Filters.saruman) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play Saruman from your draw deck";
                        }
                    });
            possibleEffects.add(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, -2, Filters.saruman) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play Saruman from your discard";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
