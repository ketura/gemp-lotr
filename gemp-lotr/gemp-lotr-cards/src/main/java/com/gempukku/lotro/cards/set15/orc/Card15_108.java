package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 6
 * Type: Minion • Orc
 * Strength: 14
 * Vitality: 3
 * Site: 4
 * Game Text: When you play Destructive Orc, you may discard a fortification for each [ORC] possession and each
 * [ORC] artifact you can spot.
 */
public class Card15_108 extends AbstractMinion {
    public Card15_108() {
        super(6, 14, 3, 4, Race.ORC, Culture.ORC, "Destructive Orc", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Culture.ORC, Filters.or(CardType.POSSESSION, CardType.ARTIFACT));
            for (int i = 0; i < count; i++)
                action.appendEffect(
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Keyword.FORTIFICATION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
