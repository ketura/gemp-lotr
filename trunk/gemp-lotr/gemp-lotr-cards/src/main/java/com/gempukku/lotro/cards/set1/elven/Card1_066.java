package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Vitality: +1
 * Game Text: Tale. Bearer must be an Elf. Skirmish: Discard this condition to make bearer strength +2.
 */
public class Card1_066 extends AbstractAttachable {
    public Card1_066() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.ELVEN, "The Tale of Gil-galad", "1_66", true);
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        Filter validTargetFilter = Filters.keyword(Keyword.ELF);

        appendAttachCardAction(actions, game, self, validTargetFilter);

        if (game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && self.getZone() == Zone.ATTACHED) {
            CostToEffectAction action = new CostToEffectAction(self, "Discard this condition to make bearer strength +2");
            action.addCost(new DiscardCardFromPlayEffect(self));
            action.addEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.sameCard(self.getAttachedTo()), 2), Phase.SKIRMISH));

            actions.add(action);
        }

        return actions;
    }
}
