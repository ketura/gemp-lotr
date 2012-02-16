package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 6
 * Vitality: 2
 * Site: 4
 * Game Text: At the start of the maneuver phase, spot another [MEN] Man and exert this minion to add a threat.
 * Then add an additional threat for each companion you spot over 4.
 */
public class Card17_063 extends AbstractMinion {
    public Card17_063() {
        super(2, 6, 2, 4, Race.MAN, Culture.MEN, "Vengeful Wild Man");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
                && PlayConditions.canSpot(game, Culture.MEN, Race.MAN)
                && PlayConditions.canSelfExert(self, game)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new AddThreatsEffect(self.getOwner(), self, 1));
            action.appendEffect(
                    new ForEachYouSpotEffect(self.getOwner(), CardType.COMPANION) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            if (spotCount > 4)
                                action.appendEffect(
                                        new AddThreatsEffect(self.getOwner(), self, spotCount - 4));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
