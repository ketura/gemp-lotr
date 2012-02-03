package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event • Assignment
 * Game Text: Make a [ROHAN] companion defender +1 and minions skirmishing that companion cannot take wounds during
 * a skirmish involving that companion until the regroup phase.
 */
public class Card15_122 extends AbstractEvent {
    public Card15_122() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Burial Mounds", Phase.ASSIGNMENT);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a ROHAN companion", Culture.ROHAN, CardType.COMPANION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new KeywordModifier(self, card, Keyword.DEFENDER, 1), Phase.REGROUP));
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new CantTakeWoundsModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(card))), Phase.REGROUP));
                    }
                });
        return action;
    }
}
