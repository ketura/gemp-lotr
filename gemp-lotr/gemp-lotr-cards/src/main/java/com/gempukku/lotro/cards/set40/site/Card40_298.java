package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Steward's Chamber
 * Set: Second Edition
 * Side: None
 * Site Number: 6
 * Shadow Number: 3
 * Card Number: 1C298
 * Game Text: Sanctuary. When the fellowship moves to this site, the Free Peoples player may exert a companion to heal
 * a companion of a different culture.
 */
public class Card40_298 extends AbstractSite {
    public Card40_298() {
        super("Steward's Chamber", Block.SECOND_ED, 6, 3, Direction.LEFT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && GameUtils.isFP(game, playerId)
                && PlayConditions.canExert(self, game, CardType.COMPANION)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            action.appendEffect(
                                    new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION, Filters.not(character.getBlueprint().getCulture())));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
