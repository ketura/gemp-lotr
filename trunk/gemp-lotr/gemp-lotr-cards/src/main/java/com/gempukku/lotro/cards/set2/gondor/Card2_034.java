package com.gempukku.lotro.cards.set2.gondor;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
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
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a companion is about to take a wound, exert a [GONDOR] companion to prevent that wound.
 */
public class Card2_034 extends AbstractResponseEvent {
    public Card2_034() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Gondor Will See It Done");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.GONDOR), Filters.type(CardType.COMPANION))
                && checkPlayRequirements(playerId, game, self, 0)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            Collection<PhysicalCard> woundedCards = woundEffect.getAffectedCardsMinusPrevented(game);
            if (Filters.filter(woundedCards, game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)).size() > 0) {
                final PlayEventAction action = new PlayEventAction(self);
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.GONDOR), Filters.type(CardType.COMPANION)));
                action.appendEffect(
                        new ChooseActiveCardEffect(playerId, "Choose companion", Filters.in(woundedCards), Filters.type(CardType.COMPANION)) {
                            @Override
                            protected void cardSelected(PhysicalCard card) {
                                action.appendEffect(
                                        new PreventEffect(woundEffect, card));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
