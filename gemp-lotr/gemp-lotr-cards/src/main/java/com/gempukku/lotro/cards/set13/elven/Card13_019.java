package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 4
 * Type: Event • Archery
 * Game Text: Spot 3 Elves, spot a Shadow race, and choose a Shadow player to wound each of that player’s minions which
 * is not of that race.
 */
public class Card13_019 extends AbstractEvent {
    public Card13_019() {
        super(Side.FREE_PEOPLE, 4, Culture.ELVEN, "Let Fly", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Race.ELF) && PlayConditions.canSpot(game, Side.SHADOW,
                new Filter() {
                    @Override
                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                        return physicalCard.getBlueprint().getRace() != null;
                    }
                });
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a race to spare", CardType.MINION,
                        new Filter() {
                            @Override
                            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                return physicalCard.getBlueprint().getRace() != null;
                            }
                        }) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard cardOfRace) {
                        action.appendEffect(
                                new ChooseOpponentEffect(playerId) {
                                    @Override
                                    protected void opponentChosen(String opponentId) {
                                        action.appendEffect(
                                                new WoundCharactersEffect(self, CardType.MINION, Filters.owner(opponentId), Filters.not(cardOfRace.getBlueprint().getRace())));
                                    }
                                });
                    }
                });
        return action;
    }
}
