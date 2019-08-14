package com.gempukku.lotro.cards.set30.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
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
 * Game Text: Wise. Each time you reconcile, you may draw 2 cards. Skirmish: Play a [GANDALF] skirmish
 * event and add a doubt to play a Dwarf from your draw deck. Skirmish: Play a [DWARVEN] skirmish
 * event to make an Orc skirmishing Bilbo strength -2.
 */
public class Card30_025 extends AbstractCompanion {
    public Card30_025() {
        super(2, 7, 4, 6, Culture.GANDALF, Race.WIZARD, null, "Gandalf", "Leader of Dwarves", true);
        addKeyword(Keyword.WISE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.reconciles(game, effectResult, playerId)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
	
    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {

            if (PlayConditions.canPlayFromHand(playerId, game, Culture.GANDALF, CardType.EVENT, Keyword.SKIRMISH)) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Play a [GANDALF] event");
                action.appendCost(new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.GANDALF, CardType.EVENT, Keyword.SKIRMISH));
                action.appendCost(new AddBurdenEffect(playerId, self, 1));
                action.appendEffect(new ChooseAndPlayCardFromDeckEffect(self.getOwner(), Race.DWARF));
                actions.add(action);
            }
            if (PlayConditions.canPlayFromHand(playerId, game, Culture.DWARVEN, CardType.EVENT, Keyword.SKIRMISH)) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Play a [DWARVEN] event");
                action.appendCost(new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.DWARVEN, CardType.EVENT, Keyword.SKIRMISH));
                action.appendEffect(new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -2,
                        CardType.MINION, Race.ORC, Filters.inSkirmishAgainst(Filters.name("Bilbo"))));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
