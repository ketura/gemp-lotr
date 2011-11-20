package com.gempukku.lotro.cards.set8.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 7
 * Type: Minion • Man
 * Strength: 14
 * Vitality: 4
 * Site: 4
 * Game Text: Corsair. Enduring. Fierce. Shadow: Exert Castamir of Umbar and play a corsair to add 2 [RAIDER] tokens to
 * a card that already has a [RAIDER] token on it.
 */
public class Card8_051 extends AbstractMinion {
    public Card8_051() {
        super(7, 14, 4, 4, Race.MAN, Culture.RAIDER, "Castamir of Umbar", true);
        addKeyword(Keyword.CORSAIR);
        addKeyword(Keyword.ENDURING);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canPlayFromHand(playerId, game, Keyword.CORSAIR)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Keyword.CORSAIR));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a card to add tokens to", Filters.hasToken(Token.RAIDER)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddTokenEffect(self, card, Token.RAIDER, 2));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
