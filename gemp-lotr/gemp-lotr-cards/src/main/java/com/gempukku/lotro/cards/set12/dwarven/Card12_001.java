package com.gempukku.lotro.cards.set12.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event • Maneuver
 * Game Text: Spot a Dwarf to choose a Shadow player who must spot a race. Discard from play all minions of all other
 * races.
 */
public class Card12_001 extends AbstractEvent {
    public Card12_001() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Argument Ready to Hand", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.DWARF);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final Filter hasRace = new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getRace() != null;
            }
        };

        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        action.insertEffect(
                                new ChooseActiveCardEffect(self, playerId, "Choose character with a race you wish to spare", Filters.character, hasRace) {
                                    @Override
                                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                                        action.insertEffect(
                                                new DiscardCardsFromPlayEffect(self, hasRace, Filters.not(card.getBlueprint().getRace())));
                                    }
                                });
                    }
                });
        return action;
    }
}
