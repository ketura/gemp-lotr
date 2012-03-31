package com.gempukku.lotro.cards.set13.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 [ROHAN] companions. When you play this, add a [ROHAN] token here for each plains site and
 * each battleground site on the adventure path. At the start of each regroup phase, add a [ROHAN] token here for each
 * of your companions that has muster.
 */
public class Card13_139 extends AbstractPermanent {
    public Card13_139() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Wind-swept Homestead", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Culture.ROHAN, CardType.COMPANION);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Zone.ADVENTURE_PATH, CardType.SITE, Filters.or(Keyword.PLAINS, Keyword.BATTLEGROUND));
            if (count > 0)
                action.appendEffect(
                        new AddTokenEffect(self, self, Token.ROHAN, count));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION, Keyword.MUSTER);
            if (count > 0)
                action.appendEffect(
                        new AddTokenEffect(self, self, Token.ROHAN, count));
            return Collections.singletonList(action);
        }
        return null;
    }
}
