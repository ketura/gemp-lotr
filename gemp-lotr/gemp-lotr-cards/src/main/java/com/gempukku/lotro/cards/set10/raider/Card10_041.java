package com.gempukku.lotro.cards.set10.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: Easterling. Fierce. When you play this minion, you may spot another Easterling to add a burden for each
 * companion you spot over 5.
 */
public class Card10_041 extends AbstractMinion {
    public Card10_041() {
        super(5, 11, 3, 4, Race.MAN, Culture.RAIDER, "Easterling Pillager", true);
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Filters.not(self), Keyword.EASTERLING)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ForEachYouSpotEffect(playerId, CardType.COMPANION) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            if (spotCount > 5)
                                action.appendEffect(
                                        new AddBurdenEffect(self, spotCount - 5));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
