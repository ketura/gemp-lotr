package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractResponseEvent;
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
import com.gempukku.lotro.logic.effects.ActivateCardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

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
public class Card4_155 extends AbstractResponseEvent {
    public Card4_155() {
        super(Side.SHADOW, Culture.ISENGARD, "Haunting Her Steps");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.ACTIVATE
                && PlayConditions.canExert(self, game, Filters.name("Grima"))) {
            ActivateCardEffect activateEffect = (ActivateCardEffect) effect;
            final PhysicalCard source = activateEffect.getSource();
            if (!activateEffect.isCancelled()
                    && Filters.or(Filters.unboundCompanion, Filters.type(CardType.ALLY)).accepts(game.getGameState(), game.getModifiersQuerying(), source)) {
                PlayEventAction action = new PlayEventAction(self);
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Grima")));
                action.appendEffect(
                        new CancelActivatedEffect(self, activateEffect));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
