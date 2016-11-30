package com.gempukku.lotro.cards.set30.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
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
 * Set: Main Deck
 * Twilight Cost: 8
 * Type: Site
 * Site: 8
 * Game Text: Mountain. Underground. Shadow: Play Smaug from your draw deck or discard pile.
 */
public class Card30_056 extends AbstractSite {
    public Card30_056() {
        super("Smaug's Den", Block.HOBBIT, 8, 8, Direction.RIGHT);
		addKeyword(Keyword.MOUNTAIN);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)) {
            ActivateCardAction action = new ActivateCardAction(self);

            List<Effect> possibleEffects = new LinkedList<Effect>();
            if (Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.name("Smaug")).size() > 0) {
                possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.name("Smaug")) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play Smaug from your draw deck";
                        }
                    });
			}
            if (PlayConditions.canPlayFromDiscard(playerId, game, Filters.name("Smaug"))) {
                possibleEffects.add(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.name("Smaug")) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Play Smaug from your discard pile";
                            }
				});
			}

            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));

            return Collections.singletonList(action);
        }
        return null;
    }
}