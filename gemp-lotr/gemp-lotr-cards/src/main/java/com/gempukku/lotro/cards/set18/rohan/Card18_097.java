package com.gempukku.lotro.cards.set18.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 0
 * Type: Possession • Shield
 * Game Text: Bearer must be a [ROHAN] Man. The minion archery total is -1. Skirmish: If bearer is Erkenbrand, you may
 * add (2) to cancel a fierce skirmish involving him. Response: If a [ROHAN] Man is about to take a wound, spot Gandalf
 * and exert bearer to prevent that wound.
 */
public class Card18_097 extends AbstractAttachableFPPossession {
    public Card18_097() {
        super(0, 0, 0, Culture.ROHAN, PossessionClass.SHIELD, "Erkenbrand's Shield", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ArcheryTotalModifier(self, Side.SHADOW, -1));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, Filters.hasAttached(self), Filters.name("Erkenbrand"))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 2));
            if (game.getGameState().isFierceSkirmishes())
                action.appendEffect(
                        new CancelSkirmishEffect(Filters.hasAttached(self)));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.ROHAN, Race.MAN)
                && PlayConditions.canSpot(game, Filters.gandalf)
                && PlayConditions.canExert(self, game, self.getAttachedTo())) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(action, self, self.getAttachedTo()));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose a ROHAN Man to prevent wound to", Culture.ROHAN, Race.MAN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
