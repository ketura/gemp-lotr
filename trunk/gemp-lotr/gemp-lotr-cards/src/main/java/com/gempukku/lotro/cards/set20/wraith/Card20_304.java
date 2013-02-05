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
 * •Ulaire Attea, Fallen King of Men
 * Ringwraith	Minion • Nazgul
 * 12	3	3
 * Fierce. Each time Ulaire Attea wins a skirmish, you may exert a companion bearing a [Ringwraith] condition.
 */
public class Card20_304 extends AbstractMinion {
    public Card20_304() {
        super(6, 12, 3, 3, Race.NAZGUL, Culture.WRAITH, Names.attea, "Fallen King of Men", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.hasAttached(CardType.CONDITION, Culture.WRAITH)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
