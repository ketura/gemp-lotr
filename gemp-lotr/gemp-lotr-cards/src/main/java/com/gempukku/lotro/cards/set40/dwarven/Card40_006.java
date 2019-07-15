package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Title: Battle Tested
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1U6
 * Game Text: Stack the top 3 cards of your draw deck onto a [DWARVEN] support area condition to make a Dwarf take no more than one wound in a skirmish.
 */
public class Card40_006 extends AbstractEvent{
    public Card40_006(Side side, int twilightCost, Culture culture, String name, Phase playableInPhase, Phase... additionalPlayableInPhases) {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Battle Tested", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canStackDeckTopCards(game, self.getOwner(), 3, Culture.DWARVEN, Zone.SUPPORT, CardType.CONDITION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, self.getOwner(), "Choose condition to stack cards on", Culture.DWARVEN, Zone.SUPPORT, CardType.CONDITION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendCost(new StackTopCardsFromDeckEffect(self, self.getOwner(), 3, card));
                    }
                });
        action.appendEffect(
                new ChooseActiveCardEffect(self, self.getOwner(), "Choose a Dwarf", Race.DWARF) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, card)));
                    }
                }
        );
        return action;
    }
}
