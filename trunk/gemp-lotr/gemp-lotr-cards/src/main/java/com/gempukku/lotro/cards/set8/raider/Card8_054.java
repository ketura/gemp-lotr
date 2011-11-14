package com.gempukku.lotro.cards.set8.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Token;
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
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Corsair. When you play this minion, you may remove 2 culture tokens to add 2 [RAIDER] tokens to a card
 * that already has a [RAIDER] token on it.
 */
public class Card8_054 extends AbstractMinion {
    public Card8_054() {
        super(3, 8, 2, 4, Race.MAN, Culture.RAIDER, "Corsair Freebooter");
        addKeyword(Keyword.CORSAIR);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, self)
                && PlayConditions.canRemoveAnyCultureTokens(game, 2, Filters.any)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, null, 2, Filters.any));
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
