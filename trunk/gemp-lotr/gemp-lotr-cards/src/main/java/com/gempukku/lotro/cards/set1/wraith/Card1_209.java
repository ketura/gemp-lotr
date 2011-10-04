package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Response: If your Nazgul wins a skirmish, transfer this condition from your
 * support area to the losing character. Limit 1 per character. Wound bearer at the start of each fellowship phase.
 * (If bearer is the Ring-bearer, add a burden instead )
 */
public class Card1_209 extends AbstractPermanent {
    public Card1_209() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.WRAITH, Zone.SHADOW_SUPPORT, "Blade Tip");
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.owner(self.getOwner()), Filters.race(Race.NAZGUL)))
                && self.getZone() == Zone.SHADOW_SUPPORT
                && game.getGameState().getSkirmish() != null && game.getGameState().getSkirmish().getFellowshipCharacter() != null
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.sameCard(game.getGameState().getSkirmish().getFellowshipCharacter()), Filters.not(Filters.hasAttached(Filters.name("Blade Tip"))))) {
            ActivateCardAction action = new ActivateCardAction(self, null);
            action.appendEffect(
                    new TransferPermanentEffect(self, game.getGameState().getSkirmish().getFellowshipCharacter()));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                && game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP
                && self.getZone() == Zone.ATTACHED) {
            boolean ringBearer = game.getModifiersQuerying().hasKeyword(game.getGameState(), self.getAttachedTo(), Keyword.RING_BEARER);
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new CardAffectsCardEffect(self, self.getAttachedTo()));
            if (ringBearer) {
                action.appendEffect(new AddBurdenEffect(self, 1));
            } else {
                action.appendEffect(new WoundCharactersEffect(self, self.getAttachedTo()));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
