package com.gempukku.lotro.cards.set30.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.modifiers.CantDiscardFromPlayModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Companion • Wizard
 * Strength: 7
 * Vitality: 4
 * Resitance: 6
 * Game Text: Wise. The twilight cost of Thorin is -2. While you can spot 2 Dwarf followers, Gandalf cannot be discarded.
 * Fellowship: Discard a Dwarf follower and add a doubt to draw 3 cards.
 */
public class Card30_026 extends AbstractCompanion {
    public Card30_026() {
        super(1, 7, 4, 6, Culture.GANDALF, Race.WIZARD, null, "Gandalf", "Friend of Thorin", true);
		addKeyword(Keyword.WISE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new TwilightCostModifier(self, Filters.name("Thorin"), -2));
        modifiers.add(
                new CantDiscardFromPlayModifier(self, "Cannot be discarded", new SpotCondition(2, Race.DWARF, CardType.FOLLOWER), self, Filters.any));
        return modifiers;
	}

	@Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canDiscardFromPlay(self, game, Race.DWARF, CardType.FOLLOWER)) {
            ActivateCardAction action = new ActivateCardAction(self);

            action.appendCost(new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Race.DWARF, CardType.FOLLOWER));
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 3));

            return Collections.singletonList(action);
        }
		return null;
    }
}