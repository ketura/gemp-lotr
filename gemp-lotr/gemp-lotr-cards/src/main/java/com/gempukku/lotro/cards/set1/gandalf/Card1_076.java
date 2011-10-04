package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PreventEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Spell. Response: If a companion is about to take a wound, spot Gandalf to prevent that wound.
 */
public class Card1_076 extends AbstractResponseEvent {
    public Card1_076() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Intimidate");
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, final PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND
                && checkPlayRequirements(playerId, game, self, 0)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            final Collection<PhysicalCard> cardsToBeWounded = woundEffect.getAffectedCardsMinusPrevented(game);

            if (Filters.filter(cardsToBeWounded, game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)).size() > 0) {
                final PlayEventAction action = new PlayEventAction(self);
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose companion", Filters.type(CardType.COMPANION), Filters.in(cardsToBeWounded)) {
                            @Override
                            protected void cardSelected(PhysicalCard companion) {
                                action.appendEffect(
                                        new PreventEffect(woundEffect, companion));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
