package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.PreventExertEffect;
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
            final ExertCharacterEffect exertEffect = (ExertCharacterEffect) effect;
            List<PhysicalCard> exertedCharacters = exertEffect.getCardsToBeExerted(game);
            if (Filters.filter(exertedCharacters, game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)).size() > 0) {
                final PlayEventAction action = new PlayEventAction(self);
                action.addEffect(
                        new ChooseActiveCardEffect(playerId, "Choose character", Filters.type(CardType.COMPANION), Filters.in(exertedCharacters)) {
                            @Override
                            protected void cardSelected(PhysicalCard card) {
                                action.addEffect(new CardAffectsCardEffect(self, card));
                                action.addEffect(
                                        new PreventExertEffect(exertEffect, card));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
