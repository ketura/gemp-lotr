package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The Bridge of Khazad-dum
 * 5	6
 * Underground.
 * Shadow: Play the Balrog from your hand or draw deck; its twilight cost is -6.
 */
public class Card20_440 extends AbstractSite {
    public Card20_440() {
        super("The Bridge of Khazad-dum", SitesBlock.SECOND_ED, 5, 6, null);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)) {
            ActivateCardAction action = new ActivateCardAction(self);

            List<Effect> possibleEffects = new LinkedList<Effect>();
            if (Filters.filter(game.getGameState().getHand(playerId), game, Filters.balrog, Filters.playable(game, -6)).size() > 0) {
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
