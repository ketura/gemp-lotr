package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Spell. Response: If a companion is about to exert, spot Gandalf to place no token for that exertion.
 */
public class Card1_085 extends AbstractResponseEvent {
    public Card1_085() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Strength of Spirit");
        addKeyword(Keyword.SPELL);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(final String playerId, LotroGame game, final Effect effect, final PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.EXERT
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"))) {
            final ExertCharactersEffect exertEffect = (ExertCharactersEffect) effect;
            Collection<PhysicalCard> exertedCharacters = exertEffect.getAffectedCardsMinusPrevented(game);
            if (Filters.filter(exertedCharacters, game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)).size() > 0) {
                final PlayEventAction action = new PlayEventAction(self);
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose character", Filters.type(CardType.COMPANION), Filters.in(exertedCharacters)) {
                            @Override
                            protected void cardSelected(PhysicalCard card) {
                                action.appendEffect(
                                        new PreventEffect(exertEffect, card));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
