package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Possession � Helm
 * Game Text: Bearer must be Gimli. He takes no more than 1 wound during each skirmish phase. Skirmish: Discard Gimli's
 * Helm to prevent all wounds to him.
 */
public class Card1_015 extends AbstractAttachableFPPossession {
    public Card1_015() {
        super(0, Culture.DWARVEN, "Gimli's Helm", "1_15", true);
        addKeyword(Keyword.HELM);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        Filter validTargetFilter = Filters.and(Filters.name("Gimli"), Filters.not(Filters.attached(Filters.keyword(Keyword.HELM))));

        appendAttachCardFromHandAction(actions, game, self, validTargetFilter);

        appendTransferPossessionAction(actions, game, self, validTargetFilter);

        if (game.getGameState().getCurrentPhase() == Phase.SKIRMISH) {
            CostToEffectAction action = new CostToEffectAction(self, "Discard Gimli's Helm to prevent all wounds to him");
            action.addCost(new DiscardCardFromPlayEffect(self));
            action.addEffect(new AddUntilEndOfPhaseModifierEffect(
                    new CantTakeWoundsModifier(self, "Prevent all wounds to him", Filters.sameCard(self.getAttachedTo())), Phase.SKIRMISH)
            );
            actions.add(action);
        }

        return actions;
    }

    @Override
    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.isWounded(effectResult, self.getAttachedTo())) {
            CostToEffectAction action = new CostToEffectAction(self, "Apply damage prevention");
            action.addEffect(new AddUntilEndOfPhaseModifierEffect(
                    new CantTakeWoundsModifier(self, "Can't take more than 1 wound during each skirmish phase", Filters.sameCard(self.getAttachedTo())), Phase.SKIRMISH));
            return Collections.<Action>singletonList(action);
        }
        return null;
    }
}
