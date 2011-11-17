package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally • Home 1 • Hobbit
 * Strength: 1
 * Vitality: 2
 * Site: 1
 * Game Text: Skirmish: Exert a Hobbit companion twice to cancel a fierce skirmish involving that Hobbit.
 */
public class Card2_101 extends AbstractAlly {
    public Card2_101() {
        super(1, Block.FELLOWSHIP, 1, 1, 2, Race.HOBBIT, Culture.SHIRE, "Filibert Bolger", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, 2, Race.HOBBIT, CardType.COMPANION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Race.HOBBIT, CardType.COMPANION) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard companion) {
                            Skirmish skirmish = game.getGameState().getSkirmish();
                            if (skirmish != null
                                    && skirmish.getFellowshipCharacter() == companion
                                    && game.getGameState().isFierceSkirmishes()) {
                                action.appendEffect(
                                        new CancelSkirmishEffect());
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
