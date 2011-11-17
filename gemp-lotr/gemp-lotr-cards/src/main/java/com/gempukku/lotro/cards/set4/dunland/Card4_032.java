package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Response: If your [DUNLAND] Man wins a skirmish, discard all Free Peoples cards borne by the companion
 * or ally he was skirmishing.
 */
public class Card4_032 extends AbstractResponseOldEvent {
    public Card4_032() {
        super(Side.SHADOW, Culture.DUNLAND, "Ravage the Defeated");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)
                && PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Culture.DUNLAND, Filters.race(Race.MAN)))
                && checkPlayRequirements(playerId, game, self, 0, false)) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            final List<PhysicalCard> losers = skirmishResult.getLosers();
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, Filters.and(Filters.attachedTo(Filters.in(losers)), Filters.side(Side.FREE_PEOPLE))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
