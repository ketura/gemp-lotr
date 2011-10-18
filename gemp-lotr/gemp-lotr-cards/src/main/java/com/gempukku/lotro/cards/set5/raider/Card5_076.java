package com.gempukku.lotro.cards.set5.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 1
 * Site: 4
 * Game Text: Southron. Ambush (2). Response: If a Southron is about to take a wound, remove (3) to prevent that wound.
 */
public class Card5_076 extends AbstractMinion {
    public Card5_076() {
        super(3, 8, 1, 4, Race.MAN, Culture.RAIDER, "Southron Traveler");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.AMBUSH, 2);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, final Effect effect, final PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND
                && game.getGameState().getTwilightPool() >= 3) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            final Collection<PhysicalCard> cardsToBeWounded = woundEffect.getAffectedCardsMinusPrevented(game);
            if (Filters.filter(cardsToBeWounded, game.getGameState(), game.getModifiersQuerying(), Keyword.SOUTHRON).size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new RemoveTwilightEffect(3));
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose Southron", Keyword.SOUTHRON, Filters.in(cardsToBeWounded)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard southron) {
                                action.appendEffect(
                                        new PreventCardEffect(woundEffect, southron));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
