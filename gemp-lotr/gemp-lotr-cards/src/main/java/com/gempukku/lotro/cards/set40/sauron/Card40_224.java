package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.logic.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: Orc Assassin
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Minion - Orc
 * Strength: 6
 * Vitality: 2
 * Home: 6
 * Card Number: 1U224
 * Game Text: Tracker. The roaming penalty for each [SAURON] minion is -1. Assignment: Spot 2 Hobbit companions to make the Free Peoples player assign a Hobbit to skirmish this minion.
 */
public class Card40_224 extends AbstractMinion {
    public Card40_224() {
        super(2, 6, 2, 6, Race.ORC, Culture.SAURON, "Orc Assassin");
        addKeyword(Keyword.TRACKER);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new RoamingPenaltyModifier(self, Filters.and(Filters.owner(self.getOwner()), Culture.SAURON, CardType.MINION), -1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSpot(game, 2, Race.HOBBIT, CardType.COMPANION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, game.getGameState().getCurrentPlayerId(), self, Race.HOBBIT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
