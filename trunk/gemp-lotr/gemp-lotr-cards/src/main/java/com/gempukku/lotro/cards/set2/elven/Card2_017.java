package com.gempukku.lotro.cards.set2.elven;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If an Elf archer wins a skirmish, make an opponent choose a minion to discard. That opponent
 * may remove (3) to prevent this.
 */
public class Card2_017 extends AbstractResponseEvent {
    public Card2_017() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Dismay Our Enemiers");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.race(Race.ELF), Filters.keyword(Keyword.ARCHER)))
                && PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)
                && checkPlayRequirements(playerId, game, self, 0)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.appendEffect(
                                    new PreventableEffect(action,
                                            new ChooseActiveCardEffect(self, opponentId, "Choose minion to discard", Filters.type(CardType.MINION)) {
                                                @Override
                                                protected void cardSelected(PhysicalCard card) {
                                                    action.appendEffect(
                                                            new DiscardCardsFromPlayEffect(self, card));
                                                }
                                            },
                                            Collections.singletonList(opponentId),
                                            new RemoveTwilightEffect(3)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
