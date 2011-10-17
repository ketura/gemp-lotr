package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Machine. Plays to your support area. Shadow: Exert an Uruk-hai to place an [ISENGARD] token on this card.
 * Skirmish: Spot an [ISENGARD] token here to heal an Uruk-hai. Discard this condition.
 */
public class Card5_057 extends AbstractPermanent {
    public Card5_057() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Scaling Ladder");
        addKeyword(Keyword.MACHINE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && PlayConditions.canExert(self, game, Race.URUK_HAI)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.URUK_HAI));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.ISENGARD));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && game.getGameState().getTokenCount(self, Token.ISENGARD) > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Race.URUK_HAI));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
