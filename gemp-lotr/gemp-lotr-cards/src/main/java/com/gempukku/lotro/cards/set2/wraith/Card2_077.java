package com.gempukku.lotro.cards.set2.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PreventEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Response: If a twilight Nazgul is about to take a wound, remove (1)
 * to prevent that wound.
 */
public class Card2_077 extends AbstractPermanent {
    public Card2_077() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.WRAITH, Zone.SHADOW_SUPPORT, "His Terrible Servants");
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), null, self, 1)
                && effect.getType() == EffectResult.Type.WOUND) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            Collection<PhysicalCard> woundedCards = woundEffect.getAffectedCardsMinusPrevented(game);
            if (Filters.filter(woundedCards, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL), Filters.keyword(Keyword.TWILIGHT)).size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self, Keyword.RESPONSE);
                action.appendCost(
                        new RemoveTwilightEffect(1));
                action.appendEffect(
                        new ChooseActiveCardEffect(playerId, "Choose twilight Nazgul", Filters.in(woundedCards), Filters.race(Race.NAZGUL), Filters.keyword(Keyword.TWILIGHT)) {
                            @Override
                            protected void cardSelected(PhysicalCard card) {
                                action.insertEffect(new PreventEffect(woundEffect, card));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
