package com.gempukku.lotro.cards.set31.troll;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.TransferPermanentEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Troll
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Discard this condition if Bilbo wins a skirmish. Regroup: Discard a
 * minion (or exert a Troll) to transfer this condition to a [DWARVEN] companion.
 * Bearer cannot be assigned to a skirmish by any player.
 */
public class Card31_065 extends AbstractPermanent {
    public Card31_065() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.GUNDABAD, "Caught in a Sack");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.name("Bilbo"))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new CantBeAssignedToSkirmishModifier(self, Filters.hasAttached(self));
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && (PlayConditions.canExert(self, game, CardType.MINION, Race.TROLL)
                    || PlayConditions.canSpot(game, CardType.MINION))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 1, Race.TROLL) {
                @Override
                public String getText(LotroGame game) {
                    return "Exert a Troll";
                }
            });
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION) {
                @Override
                public String getText(LotroGame game) {
                    return "Discard a minion";
                }
            });
            action.appendCost(new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a [DWARVEN] companion", Culture.DWARVEN, CardType.COMPANION, Filters.not(Filters.hasAttached(self))) {
                @Override
                protected void cardSelected(LotroGame game, PhysicalCard card) {
                    action.appendEffect(
                            new TransferPermanentEffect(self, card));
                }
            });
            return Collections.singletonList(action);
        }
        return null;
    }
}
