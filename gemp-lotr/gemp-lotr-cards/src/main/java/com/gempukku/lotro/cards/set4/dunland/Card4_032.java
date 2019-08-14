package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Response: If your [DUNLAND] Man wins a skirmish, discard all Free Peoples cards borne by the companion
 * or ally he was skirmishing.
 */
public class Card4_032 extends AbstractResponseEvent {
    public Card4_032() {
        super(Side.SHADOW, 1, Culture.DUNLAND, "Ravage the Defeated");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)
                && TriggerConditions.winsSkirmish(game, effectResult, Filters.and(Culture.DUNLAND, Race.MAN))
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            CharacterWonSkirmishResult skirmishResult = (CharacterWonSkirmishResult) effectResult;
            final Set<PhysicalCard> losers = skirmishResult.getInvolving();
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self.getOwner(), self, Filters.and(Filters.attachedTo(Filters.in(losers)), Side.FREE_PEOPLE)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
