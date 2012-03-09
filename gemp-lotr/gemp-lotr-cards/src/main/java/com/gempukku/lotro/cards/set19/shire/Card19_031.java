package com.gempukku.lotro.cards.set19.shire;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndTransferAttachableEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CardTransferredResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ages End
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Bearer must be a [SHIRE] companion. Each time bearer wins a skirmish, you may transfer this condition
 * to a fellowship companion. Each time this condition is transferred to a companion, heal that companion.
 */
public class Card19_031 extends AbstractAttachable {
    public Card19_031() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.SHIRE, null, "Rabbit Stew");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.SHIRE, CardType.COMPANION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self.getAttachedTo())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndTransferAttachableEffect(action, playerId, self, null, Filters.and(Keyword.FELLOWSHIP, CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, CardType.COMPANION)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, ((CardTransferredResult) effectResult).getTransferredTo()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
