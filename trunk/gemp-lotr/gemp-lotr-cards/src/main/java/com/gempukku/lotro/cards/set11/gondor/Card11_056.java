package com.gempukku.lotro.cards.set11.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Make a ranger strength +2 (or +3 if he or she is skirmishing a minion who has a damage bonus).
 */
public class Card11_056 extends AbstractEvent {
    public Card11_056() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Battle Cry", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a ranger", Keyword.RANGER) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int bonus = PlayConditions.canSpot(game, card, Filters.inSkirmishAgainst(CardType.MINION, Keyword.DAMAGE)) ? 3 : 2;
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, bonus), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
