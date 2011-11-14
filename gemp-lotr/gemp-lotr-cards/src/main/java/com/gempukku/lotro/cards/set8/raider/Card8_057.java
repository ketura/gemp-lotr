package com.gempukku.lotro.cards.set8.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: Corsair. When you play this minion, if you can spot another corsair, you may discard a possession to add
 * 2 [RAIDER] tokens to a card that already has a [RAIDER] token on it.
 */
public class Card8_057 extends AbstractMinion {
    public Card8_057() {
        super(4, 9, 2, 4, Race.MAN, Culture.RAIDER, "Corsair Marauder");
        addKeyword(Keyword.CORSAIR);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Filters.not(self), Keyword.CORSAIR)
                && PlayConditions.canDiscardFromPlay(self, game, CardType.POSSESSION)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a card to add tokens to", Filters.hasToken(Token.RAIDER)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddTokenEffect(self, self, Token.RAIDER, 2));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
