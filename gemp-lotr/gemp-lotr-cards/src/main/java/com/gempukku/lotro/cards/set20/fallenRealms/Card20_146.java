package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Summoned by Sauron
 * Fallen Realms	Condition • Support Area
 * To play, spot an Easterling.
 * At the end of the Shadow phase, if you can spot more companions than minions, exert an Easterling to play
 * an Easterling from your discard pile.
 */
public class Card20_146 extends AbstractPermanent {
    public Card20_146() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.FALLEN_REALMS, Zone.SUPPORT, "Summoned by Sauron", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Keyword.EASTERLING);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.endOfPhase(game, effectResult, Phase.SHADOW)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION)>
                Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.MINION)
            && PlayConditions.canExert(self, game, Keyword.EASTERLING)
                && PlayConditions.canPlayFromDiscard(self.getOwner(), game, Keyword.EASTERLING)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Keyword.EASTERLING));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(self.getOwner(), game, Keyword.EASTERLING));
            return Collections.singletonList(action);
        }
        return null;
    }
}
