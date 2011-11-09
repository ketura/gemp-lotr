package com.gempukku.lotro.cards.set6.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Regroup: Exert a Nazgul twice to play a [WRAITH] or [SAURON] minion. That
 * minion's twilight cost is -8 and it comes into play exhausted.
 */
public class Card6_084 extends AbstractPermanent {
    public Card6_084() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Spied From Above");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.REGROUP, self, 0)
                && PlayConditions.canExert(self, game, 2, Race.NAZGUL)
                && PlayConditions.canPlayFromHand(playerId, game, -8, CardType.MINION, Filters.or(Culture.WRAITH, Culture.SAURON))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Race.NAZGUL));
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), -8, CardType.MINION, Filters.or(Culture.WRAITH, Culture.SAURON)) {
                        @Override
                        protected void cardPlayed(PhysicalCard cardPlayed) {
                            action.appendEffect(
                                    new ExhaustCharacterEffect(self, action, cardPlayed));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
