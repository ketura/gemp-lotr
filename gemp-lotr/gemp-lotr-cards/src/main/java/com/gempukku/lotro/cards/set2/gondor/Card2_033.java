package com.gempukku.lotro.cards.set2.gondor;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
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
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a [GONDOR] companion wins a skirmish, discard an exhausted Orc. That minion's owner may
 * remove (3) to prevent this.
 */
public class Card2_033 extends AbstractResponseEvent {
    public Card2_033() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Flee in Terror");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)
                && PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.culture(Culture.GONDOR), Filters.type(CardType.COMPANION)))
                && checkPlayRequirements(playerId, game, self, 0)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a minion", Filters.not(Filters.canExert()), Filters.race(Race.ORC)) {
                        @Override
                        protected void cardSelected(PhysicalCard orc) {
                            action.insertEffect(
                                    new PreventableEffect(action,
                                            new DiscardCardsFromPlayEffect(self, orc),
                                            Collections.singletonList(orc.getOwner()),
                                            new RemoveTwilightEffect(3)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
