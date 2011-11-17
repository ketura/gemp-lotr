package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time a companion or ally loses a skirmish involving a [DUNLAND] Man,
 * you may place a [DUNLAND] token on this card. Maneuver: Discard a Free Peoples possession for each [DUNLAND] token
 * here (limit 3). Discard this condition.
 */
public class Card4_034 extends AbstractPermanent {
    public Card4_034() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.DUNLAND, Zone.SUPPORT, "Secret Folk", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.losesSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.or(CardType.COMPANION, CardType.ALLY))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.DUNLAND));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 0)) {
            ActivateCardAction action = new ActivateCardAction(self);
            int possesions = Math.min(3, game.getGameState().getTokenCount(self, Token.DUNLAND));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, possesions, possesions, CardType.POSSESSION, Side.FREE_PEOPLE));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
