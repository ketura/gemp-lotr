package com.gempukku.lotro.cards.set6.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.LiberateASiteEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Game Text: Bearer must be an Elf companion. Each time bearer wins a skirmish, you may draw a card.
 * Response: If bearer wins a skirmish, discard this possession to liberate a site.
 */
public class Card6_014 extends AbstractAttachableFPPossession {
    public Card6_014() {
        super(1, 0, 0, Culture.ELVEN, PossessionClass.HAND_WEAPON, "Banner of Elbereth");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Race.ELF, CardType.COMPANION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(effectResult, self.getAttachedTo())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardEffect(playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(effectResult, self.getAttachedTo())
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new LiberateASiteEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
