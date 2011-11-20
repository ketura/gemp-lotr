package com.gempukku.lotro.cards.set9.gollum;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Reflections
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Minion
 * Strength: 5
 * Vitality: 4
 * Site: 3
 * Game Text: Shadow: Exert Gollum twice to exert the Ring-bearer. Shadow: Remove a burden to add (1). Skirmish: Remove
 * a burden to add (2). Regroup: Remove a burden to add (3). Regroup: Exert Gollum twice to wound the Ring-bearer.
 */
public class Card9_028 extends AbstractMinion {
    public Card9_028() {
        super(2, 5, 4, 3, null, Culture.GOLLUM, "Gollum", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
            if (PlayConditions.canSelfExert(self, 2, game)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Exert the Ring-bearer");
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Keyword.RING_BEARER));
                actions.add(action);
            }
            if (PlayConditions.canRemoveBurdens(game, self, 1)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Add (1)");
                action.appendCost(
                        new RemoveBurdenEffect(self));
                action.appendEffect(
                        new AddTwilightEffect(self, 1));
                actions.add(action);
            }
            return actions;
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canRemoveBurdens(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(self));
            action.appendEffect(
                    new AddTwilightEffect(self, 2));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
            if (PlayConditions.canRemoveBurdens(game, self, 1)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Add (3)");
                action.appendCost(
                        new RemoveBurdenEffect(self));
                action.appendEffect(
                        new AddTwilightEffect(self, 3));
                actions.add(action);
            }
            if (PlayConditions.canSelfExert(self, 2, game)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Wound the Ring-bearer");
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendEffect(
                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Keyword.RING_BEARER));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
