package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ReinforceTokenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot an [URUK-HAI] minion. When you play this, if you spot a battleground site on the adventure
 * path, reinforce 3 [URUK-HAI] tokens. Each time the Free Peoples player plays the fellowship’s next site, you may
 * discard a condition from play.
 */
public class Card13_164 extends AbstractPermanent {
    public Card13_164() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.URUK_HAI, "Fearless Approach");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.URUK_HAI, CardType.MINION);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, CardType.SITE, Zone.ADVENTURE_PATH, Keyword.BATTLEGROUND)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ReinforceTokenEffect(self, self.getOwner(), Token.URUK_HAI, 3));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult,
                CardType.SITE,
                Filters.owner(game.getGameState().getCurrentPlayerId()),
                Filters.siteNumber(game.getGameState().getCurrentSiteNumber() + 1))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
