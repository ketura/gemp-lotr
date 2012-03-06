package com.gempukku.lotro.cards.set18.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.KillEffect;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 10
 * Type: Event • Regroup
 * Game Text: To play, spot Shelob. The Free Peoples player chooses two companions in play (except the Ring-bearer).
 * He or she then chooses to place one of those companions in the dead pile and return the other to his or her hand.
 */
public class Card18_035 extends AbstractEvent {
    public Card18_035() {
        super(Side.SHADOW, 10, Culture.GOLLUM, "Sting of Shelob", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.name("Shelob"));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, game.getGameState().getCurrentPlayerId(), "Choose companion to kill", CardType.COMPANION, Filters.not(Filters.ringBearer)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new KillEffect(self, KillEffect.Cause.CARD_EFFECT));
                    }
                });
        action.appendEffect(
                new ChooseAndReturnCardsToHandEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION, Filters.not(Filters.ringBearer)));
        return action;
    }
}
