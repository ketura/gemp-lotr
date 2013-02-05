package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 6
 * •Ulaire Toldea, Rider in Black
 * Ringwraith	Minion • Nazgul
 * 12	3	3
 * Fierce.
 * Each time Ulaire Toldea wins a skirmish, you may exert a companion with 5 resistance or less.
 */
public class Card20_311 extends AbstractMinion {
    public Card20_311() {
        super(6, 12, 3, 3, Race.NAZGUL, Culture.WRAITH, Names.toldea, "Rider in Black", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.maxResistance(5)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
