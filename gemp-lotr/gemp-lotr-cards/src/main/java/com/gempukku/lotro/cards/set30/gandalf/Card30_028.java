package com.gempukku.lotro.cards.set30.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.modifiers.CantDiscardFromPlayModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

 /**
 * Set: Main Deck
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 7
 * Vitality: 4
 * Resistance: 6
 * Game Text: Wise. Gandalf cannot be discarded. At the start of the fellowship phase, you may add a doubt to play a 
 * Dwarf companion from your draw deck.
 */
public class Card30_028 extends AbstractCompanion {
    public Card30_028() {
        super(4, 7, 4, 6, Culture.GANDALF, Race.WIZARD, null, "Gandalf", "Leader of the Company", true);
        addKeyword(Keyword.WISE);
    }
	
	@Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new CantDiscardFromPlayModifier(self, "Gandalf cannot be discarded", Filters.gandalf, Filters.any));
	}

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new AddBurdenEffect(self.getOwner(), self, 1));;
            action.appendEffect(
					new ChooseAndPlayCardFromDeckEffect(playerId, Culture.DWARVEN, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
