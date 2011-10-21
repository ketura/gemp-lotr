package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelActivatedEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ActivateCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a special ability of an ally or unbound companion is used, exert Grima to cancel that action.
 */
public class Card4_155 extends AbstractResponseOldEvent {
    public Card4_155() {
        super(Side.SHADOW, Culture.ISENGARD, "Haunting Her Steps");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.activated(game, effectResult, Filters.or(CardType.ALLY, Filters.unboundCompanion))
                && PlayConditions.canExert(self, game, Filters.name("Grima"))) {
            ActivateCardResult activateEffect = (ActivateCardResult) effectResult;
            PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Grima")));
            action.appendEffect(
                    new CancelActivatedEffect(self, activateEffect));
            return Collections.singletonList(action);
        }
        return null;
    }
}
