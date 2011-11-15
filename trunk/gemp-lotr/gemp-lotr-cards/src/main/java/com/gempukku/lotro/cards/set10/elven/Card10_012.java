package com.gempukku.lotro.cards.set10.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 Elves. Each time you lose initiative, make a minion strength -4 until the regroup phase.
 */
public class Card10_012 extends AbstractPermanent {
    public Card10_012() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Glimpse of Fate", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 2, Race.ELF);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.loseInitiative(effectResult, Side.FREE_PEOPLE)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, self.getOwner(), "Choose a minion", CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new StrengthModifier(self, card, -4), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
