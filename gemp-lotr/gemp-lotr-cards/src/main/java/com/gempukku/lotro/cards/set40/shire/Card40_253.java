package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Hobbit Party Guest
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally - Hobbit - Shire
 * Strength: 2
 * Vitality: 2
 * Card Number: 1C253
 * Game Text: At the start of each of your turns, heal this ally. Fellowship: Exert this ally to heal another Shire ally.
 */
public class Card40_253 extends AbstractAlly {
    public Card40_253() {
        super(1, SitesBlock.SECOND_ED, 0, 2, 2, Race.HOBBIT, Culture.SHIRE, "Hobbit Party Guest");
        addKeyword(Keyword.SHIRE);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Filters.not(self), CardType.ALLY, Keyword.SHIRE));
            return Collections.singletonList(action);
        }
        return null;
    }
}
