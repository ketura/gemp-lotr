package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndStackCardsFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 2
 * Site: 4
 * Game Text: Each time this minion wins a skirmish, you may stack a [MEN] minion from your discard pile on
 * a [MEN] possession in your support area.
 */
public class Card17_062 extends AbstractMinion {
    public Card17_062() {
        super(4, 10, 2, 4, Race.MAN, Culture.MEN, "Vengeful Savage");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a MEN possession", Culture.MEN, CardType.POSSESSION, Filters.owner(playerId), Zone.SUPPORT) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new ChooseAndStackCardsFromDiscardEffect(action, playerId, 1, 1, card, Culture.MEN, CardType.MINION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
