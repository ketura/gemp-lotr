package com.gempukku.lotro.cards.set5.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Spot 3 valiant Men to make one of those Men defender +1 until the regroup phase.
 */
public class Card5_086 extends AbstractEvent {
    public Card5_086() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "No Rest for the Weary", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, 3, Race.MAN, Keyword.VALIANT);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose Valiant Man", Race.MAN, Keyword.VALIANT) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new KeywordModifier(self, Filters.sameCard(card), Keyword.DEFENDER, 1), Phase.REGROUP));
                    }
                });
        return action;
    }
}
