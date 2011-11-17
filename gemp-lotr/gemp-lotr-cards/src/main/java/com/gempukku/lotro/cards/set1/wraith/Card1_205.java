package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Event
 * Game Text: Maneuver: Exert a Nazgul to discard a Free Peoples possession or Free Peoples condition. If you can spot
 * no such card, discard an ally or companion (except the Ring-bearer) instead.
 */
public class Card1_205 extends AbstractOldEvent {
    public Card1_205() {
        super(Side.SHADOW, Culture.WRAITH, "Beauty Is Fading", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.NAZGUL));
        boolean firstEffect = Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE, Filters.or(CardType.POSSESSION, CardType.CONDITION));
        if (firstEffect) {
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Free Peoples possession or condition", Side.FREE_PEOPLE, Filters.or(CardType.POSSESSION, CardType.CONDITION)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard fpCard) {
                            action.appendEffect(
                                    new DiscardCardsFromPlayEffect(self, fpCard));
                        }
                    });
        } else {
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an ally or non Ring-Bearer companion", Filters.or(CardType.ALLY, CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER))) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard fpCard) {
                            action.appendEffect(
                                    new DiscardCardsFromPlayEffect(self, fpCard));
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
