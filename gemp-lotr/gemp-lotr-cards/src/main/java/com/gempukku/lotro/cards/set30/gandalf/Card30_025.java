package com.gempukku.lotro.cards.set30.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
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
 * Game Text: Wise. At the start of each of your turns, you may exert Gandalf to play a Dwarf companion
 * from your draw deck. Maneuver: Discard 2 Dwarf followers to play Gandalf from your discard pile.
 */
public class Card30_025 extends AbstractCompanion {
    public Card30_025() {
        super(4, 7, 4, 6, Culture.GANDALF, Race.WIZARD, null, "Gandalf", "Leader of Dwarves", true);
        addKeyword(Keyword.WISE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
					new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(self.getOwner(), Race.DWARF, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
	
    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.MANEUVER)
            && (PlayConditions.canDiscardFromPlay(self, game, 2, Race.DWARF, CardType.FOLLOWER))
            && (PlayConditions.canPlayFromDiscard(playerId, game, self))) {
                ActivateCardAction replayGandalf = new ActivateCardAction(self);			
                replayGandalf.appendCost(
                        new ChooseAndDiscardCardsFromPlayEffect(replayGandalf, playerId, 2, 2, Race.DWARF, CardType.FOLLOWER));
                replayGandalf.appendEffect(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, self));
                return Collections.singletonList(replayGandalf);
        }
        return null;
    }
}
