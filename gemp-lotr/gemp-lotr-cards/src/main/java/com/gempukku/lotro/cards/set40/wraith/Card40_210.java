package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Ulaire Toldea, Rider in Black
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 6
 * Type: Minion - Nazgul
 * Strength: 12
 * Vitality: 3
 * Home: 3
 * Card Number: 1R210
 * Game Text: Fierce. Each time Ulaire Toldea wins a skirmish, you may exert a companion with 3 or less resistance.
 */
public class Card40_210 extends AbstractMinion {
    public Card40_210() {
        super(6, 12, 3, 3, Race.NAZGUL, Culture.WRAITH, Names.toldea, "Rider in Black", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.maxResistance(3)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
