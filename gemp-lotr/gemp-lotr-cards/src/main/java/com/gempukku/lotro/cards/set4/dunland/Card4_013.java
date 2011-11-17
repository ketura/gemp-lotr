package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 1
 * Site: 3
 * Game Text: Each time this minion wins a skirmish, you may discard a Free Peoples possession (or 2 possessions
 * if you spot 6 companions).
 */
public class Card4_013 extends AbstractMinion {
    public Card4_013() {
        super(3, 9, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Pillager");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int max = (Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) >= 6) ? 2 : 1;
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, max, Side.FREE_PEOPLE, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
