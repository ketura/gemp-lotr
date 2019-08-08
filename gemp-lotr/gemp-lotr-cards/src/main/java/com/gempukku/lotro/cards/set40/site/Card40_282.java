package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Midgewater Marshes
 * Set: Second Edition
 * Side: None
 * Site Number: 2
 * Shadow Number: 1
 * Card Number: 1U282
 * Game Text: Marsh. When the fellowship moves to this site, each Hobbit companion must exert.
 */
public class Card40_282 extends AbstractSite {
    public Card40_282() {
        super("Midgewater Marshes", Block.SECOND_ED, 2, 1, Direction.LEFT);
        addKeyword(Keyword.MARSH);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(action, self, Race.HOBBIT, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
