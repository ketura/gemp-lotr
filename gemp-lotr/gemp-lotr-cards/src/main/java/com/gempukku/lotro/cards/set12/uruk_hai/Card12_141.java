package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.CancelActivatedEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ActivateCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Response: If an ally or companion uses a special ability, discard this condition and exert an [URUK-HAI]
 * lurker to prevent that.
 */
public class Card12_141 extends AbstractPermanent {
    public Card12_141() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.URUK_HAI, Zone.SUPPORT, "Dark Alliance");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.activated(game, effectResult, Filters.or(CardType.ALLY, CardType.COMPANION))
                && PlayConditions.canSelfDiscard(self, game)
                && PlayConditions.canExert(self, game, Culture.URUK_HAI, Keyword.LURKER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.URUK_HAI, Keyword.LURKER));
            action.appendEffect(
                    new CancelActivatedEffect(self, (ActivateCardResult) effectResult));
            return Collections.singletonList(action);
        }
        return null;
    }
}
