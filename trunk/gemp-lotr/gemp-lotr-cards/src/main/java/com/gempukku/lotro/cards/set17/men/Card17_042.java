package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: When you play this minion, you may spot 2 other [MEN] Men to add (1) for each possession you spot.
 */
public class Card17_042 extends AbstractMinion {
    public Card17_042() {
        super(3, 8, 2, 4, Race.MAN, Culture.MEN, "Easterling Dispatcher");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, 2, Filters.not(self), Culture.MEN, Race.MAN)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ForEachYouSpotEffect(playerId, CardType.POSSESSION) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            action.appendEffect(
                                    new AddTwilightEffect(self, spotCount));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
