package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.CantTakeArcheryWoundsModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 1
 * Type: Event • Archery
 * Game Text: Spot a [MEN] minion to make the minion archery total +1 and make the Free Peoples player choose
 * 3 companions. Other companions cannot take archery wounds.
 */
public class Card11_096 extends AbstractEvent {
    public Card11_096() {
        super(Side.SHADOW, 1, Culture.MEN, "Precision Targeting", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.MEN, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilEndOfPhaseModifierEffect(
                        new ArcheryTotalModifier(self, Side.SHADOW, 1)));
        action.appendEffect(
                new ChooseActiveCardsEffect(self, game.getGameState().getCurrentPlayerId(), "Choose companions who will be taking archery wound", 3, 3, CardType.COMPANION) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new CantTakeArcheryWoundsModifier(self, Filters.and(CardType.COMPANION, Filters.not(Filters.in(cards))))));
                    }
                });
        return action;
    }
}
