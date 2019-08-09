package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Banks of Nen Hithoel
 * Set: Second Edition
 * Side: None
 * Site Number: 3
 * Shadow Number: 0
 * Card Number: 1U283
 * Game Text: River. Sanctuary. At the start of the fellowship phase, heal a companion for each culture over 2 you can spot.
 */
public class Card40_283 extends AbstractSite {
    public Card40_283() {
        super("Banks of Nen Hithoel", Block.SECOND_ED, 3, 0, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int cultureCount = GameUtils.getSpottableCulturesCount(game, Filters.any);
            int healCount = Math.max(0, cultureCount - 2);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, GameUtils.getFreePeoplePlayer(game), healCount, healCount, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
