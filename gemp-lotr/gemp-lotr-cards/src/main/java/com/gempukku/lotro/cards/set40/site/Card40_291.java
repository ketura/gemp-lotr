package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
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
 * Title: The Bridge of Khazad-dum
 * Set: Second Edition
 * Side: None
 * Site Number: 5
 * Shadow Number: 6
 * Card Number: 1U291
 * Game Text: Underground. Shadow: Play the Balrog from your hand or draw deck; its twilight cost is -6.
 */
public class Card40_291 extends AbstractSite {
    public Card40_291() {
        super("The Bridge of Khazad-dun", Block.SECOND_ED, 5, 6, Direction.LEFT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)) {
            ActivateCardAction action = new ActivateCardAction(self);

            List<Effect> possibleEffects = new LinkedList<Effect>();
            if (Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.balrog, Filters.playable(game, -6)).size() > 0) {
                // Play from hand
                possibleEffects.add(
                        new ChooseAndPlayCardFromHandEffect(playerId, game, -6, Filters.balrog) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Play The Balrog from hand";
                            }
                        });
            }

            // Play from deck
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, -6, Filters.balrog) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play The Balrog from deck";
                        }
                    });

            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));

            return Collections.singletonList(action);
        }
        return null;
    }
}
