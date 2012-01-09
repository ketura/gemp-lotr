package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot a [MEN] minion. When you play this, if you spot a plains site on the adventure path,
 * reinforce 3 [MEN] tokens. Each time the Free Peoples player plays the fellowship’s next site, you may discard
 * a possession from play.
 */
public class Card13_083 extends AbstractPermanent {
    public Card13_083() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MEN, Zone.SUPPORT, "Caravan From the South");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.MEN, CardType.MINION);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, CardType.SITE, Keyword.PLAINS, Zone.ADVENTURE_PATH)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ReinforceTokenEffect(self, self.getOwner(), Token.MEN, 3));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.owner(game.getGameState().getCurrentPlayerId()), CardType.SITE, Filters.siteNumber(game.getGameState().getCurrentSiteNumber() + 1))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
