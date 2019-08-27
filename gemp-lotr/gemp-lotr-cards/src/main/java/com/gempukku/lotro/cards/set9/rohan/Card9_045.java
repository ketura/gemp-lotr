package com.gempukku.lotro.cards.set9.rohan;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AddSignetModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Possession
 * Strength: +1
 * Game Text: Bearer must be Merry or Pippin. Bearer gains the Theoden signet. Response: If bearer wins a skirmish,
 * exert him to discard each minion involved in that skirmish.
 */
public class Card9_045 extends AbstractAttachableFPPossession {
    public Card9_045() {
        super(1, 1, 0, Culture.ROHAN, null, "Horn of the Mark", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.or(Filters.name("Merry"), Filters.name("Pippin"));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AddSignetModifier(self, Filters.hasAttached(self), Signet.THEODEN));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self.getAttachedTo())
                && PlayConditions.canExert(self, game, self.getAttachedTo())) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(action, self, self.getAttachedTo()));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self.getOwner(), self, CardType.MINION, Filters.inSkirmish));
            return Collections.singletonList(action);
        }
        return null;
    }
}
