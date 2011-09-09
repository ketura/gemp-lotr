package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Event
 * Game Text: Maneuver: Exert a Nazgul to discard a Free Peoples possession or Free Peoples condition. If you can spot
 * no such card, discard an ally or companion (except the Ring-bearer) instead.
 */
public class Card1_205 extends AbstractEvent {
    public Card1_205() {
        super(Side.SHADOW, Culture.WRAITH, "Beauty Is Fading", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.NAZGUL), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose a Nazgul", true, Filters.keyword(Keyword.NAZGUL), Filters.canExert()));
        boolean firstEffect = Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.side(Side.FREE_PEOPLE), Filters.or(Filters.type(CardType.POSSESSION), Filters.type(CardType.CONDITION)));
        if (firstEffect) {
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a Free Peoples possession or condition", Filters.side(Side.FREE_PEOPLE), Filters.or(Filters.type(CardType.POSSESSION), Filters.type(CardType.CONDITION))) {
                        @Override
                        protected void cardSelected(PhysicalCard fpCard) {
                            action.addEffect(
                                    new DiscardCardFromPlayEffect(self, fpCard));
                        }
                    });
        } else {
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose an ally or non Ring-Bearer companion", Filters.or(Filters.type(CardType.ALLY), Filters.type(CardType.COMPANION)), Filters.not(Filters.keyword(Keyword.RING_BEARER))) {
                        @Override
                        protected void cardSelected(PhysicalCard fpCard) {
                            action.addEffect(
                                    new DiscardCardFromPlayEffect(self, fpCard));
                        }
                    });
        }

        return action;
    }

    @Override
    public int getTwilightCost() {
        return 5;
    }
}
