package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title: *Frodo, Extraordinarily Resilient
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion - Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10R
 * Card Number: 1R249
 * Game Text: Ring-bound. At the start of each of your turn, you may exert Frodo to heal a companion of another culture
 * or exert a companion of another culture to heal Frodo.
 */
public class Card40_249 extends AbstractCompanion {
    public Card40_249() {
        super(0, 3, 4, 10, Culture.SHIRE, Race.HOBBIT, null, "Frodo",
                "Extraordinarily Resilient", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)
                && (PlayConditions.canSelfExert(self, game) || PlayConditions.canExert(self, game, CardType.COMPANION, Filters.not(Culture.SHIRE)))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            List<Effect> possibleCosts = new ArrayList<Effect>(2);
            possibleCosts.add(
                    new SelfExertEffect(action, self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert Frodo to heal a companion of another culture";
                        }

                        @Override
                        protected void forEachExertedByEffect(PhysicalCard physicalCard) {
                            action.appendEffect(
                                    new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION, Filters.not(Culture.SHIRE)));
                        }
                    });
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Culture.SHIRE)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert a companion of another culture to heal Frodo";
                        }

                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            action.appendEffect(
                                    new HealCharactersEffect(self, self));
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            return Collections.singletonList(action);
        }
        return null;
    }
}
