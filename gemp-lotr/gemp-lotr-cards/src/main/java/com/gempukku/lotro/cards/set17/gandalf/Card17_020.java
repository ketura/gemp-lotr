package com.gempukku.lotro.cards.set17.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Follower
 * Strength: +1
 * Resistance: +1
 * Game Text: Aid - Add a threat. (At the start of the maneuver phase, you may add a threat to transfer this to
 * a companion.) If bearer is a [GANDALF] Wizard, each time bearer wins a skirmish, you may exert a minion (if that
 * minion is a Wizard, wound it instead).
 */
public class Card17_020 extends AbstractFollower {
    public Card17_020() {
        super(Side.FREE_PEOPLE, 1, 1, 0, 1, Culture.GANDALF, "Gwaihir", "The Windlord", true);
    }

    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return PlayConditions.canAddThreat(game, self, 1);
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new AddThreatsEffect(self.getOwner(), self, 1));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self), Culture.GANDALF, Race.WIZARD)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION,
                            Filters.or(
                                    Filters.and(Race.WIZARD, Filters.canTakeWounds(self, 1)),
                                    Filters.and(Filters.not(Race.WIZARD), Filters.canExert(self))
                            )) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            if (card.getBlueprint().getRace() == Race.WIZARD)
                                action.appendEffect(
                                        new WoundCharactersEffect(self, card));
                            else
                                action.appendEffect(
                                        new ExertCharactersEffect(action, self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
