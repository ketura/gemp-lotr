package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title: *The Witch-king, Greatest of the Nine
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 8
 * Type: Minion - Nazgul
 * Strength: 14
 * Vitality: 4
 * Home: 3
 * Card Number: 1R211
 * Game Text: Fierce. Each time The Witch-king wins a skirmish, the Free Peoples player must exert the Ring-bearer or add a burden.
 */
public class Card40_211 extends AbstractMinion {
    public Card40_211() {
        super(8, 14, 4, 3, Race.NAZGUL, Culture.WRAITH, Names.witchKing, "Greatest of the Nine", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            final String freePeoplePlayer = GameUtils.getFreePeoplePlayer(game);

            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new ArrayList<Effect>(2);
            possibleEffects.add(
                    new AddBurdenEffect(freePeoplePlayer, self, 1));
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, GameUtils.getFreePeoplePlayer(game), 1, 1, Filters.ringBearer));
            action.appendEffect(
                    new ChoiceEffect(action, freePeoplePlayer, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
