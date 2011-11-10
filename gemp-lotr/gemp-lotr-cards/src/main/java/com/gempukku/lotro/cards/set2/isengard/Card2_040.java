package com.gempukku.lotro.cards.set2.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, spot an [ISENGARD] minion. Plays to your support area. Each time a [SHIRE] ally exerts, add (1).
 */
public class Card2_040 extends AbstractPermanent {
    public Card2_040() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Demands of the Sackville-Bagginses");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.EXERT) {
            ExertResult exertResult = (ExertResult) effectResult;
            Collection<PhysicalCard> exertedShireAllies = Filters.filter(exertResult.getExertedCards(), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SHIRE), Filters.type(CardType.ALLY));
            List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
            for (PhysicalCard exertedShireAlly : exertedShireAllies) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new AddTwilightEffect(self, 1));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
