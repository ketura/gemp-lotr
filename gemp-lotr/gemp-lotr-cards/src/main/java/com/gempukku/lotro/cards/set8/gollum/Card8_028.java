package com.gempukku.lotro.cards.set8.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Possession • Support Area
 * Game Text: Skirmish: Remove a threat or a burden to transfer this possession to a companion skirmishing Shelob.
 * Each time bearer is assigned to a skirmish, bearer must exert.
 */
public class Card8_028 extends AbstractPermanent {
    public Card8_028() {
        super(Side.SHADOW, 1, CardType.POSSESSION, Culture.GOLLUM, Zone.SUPPORT, "Spider Poison");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && (PlayConditions.canRemoveThreat(game, self, 1) || PlayConditions.canRemoveBurdens(game, self, 1))
                && self.getZone() == Zone.SUPPORT) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new RemoveThreatsEffect(self, 1) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove a threat";
                        }
                    });
            possibleCosts.add(
                    new RemoveBurdenEffect(self));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION, Filters.inSkirmishAgainst(Filters.name("Shelob"))) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new TransferPermanentEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assigned(game, effectResult, null, Filters.any, Filters.hasAttached(self))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(self, Filters.hasAttached(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
