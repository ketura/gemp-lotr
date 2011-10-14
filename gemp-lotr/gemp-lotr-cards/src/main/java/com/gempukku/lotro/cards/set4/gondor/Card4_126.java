package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
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
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time a [GONDOR] Man wins a skirmish, you may place a [GONDOR] token here.
 * Maneuver: Exert a minion for each [GONDOR] token here (limit 3). Discard this condition.
 */
public class Card4_126 extends AbstractPermanent {
    public Card4_126() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Ithilien Trap", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.culture(Culture.GONDOR), Filters.race(Race.MAN)))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.GONDOR));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            int tokens = game.getGameState().getTokenCount(self, Token.GONDOR);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, tokens, tokens, Filters.type(CardType.MINION)));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
