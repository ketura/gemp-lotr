package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 9
 * Type: Minion • Troll
 * Strength: 16
 * Vitality: 4
 * Site: 6
 * Game Text: Besieger. Fierce. The twilight cost of this minion is -1 for each [SAURON] engine you spot. When you play
 * this minion at sites 5K to 9K, you may discard a Free Peoples condition.
 */
public class Card8_102 extends AbstractMinion {
    public Card8_102() {
        super(9, 16, 4, 6, Race.TROLL, Culture.SAURON, "Great Hill Troll");
        addKeyword(Keyword.BESIEGER);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -Filters.countSpottable(gameState, modifiersQuerying, Culture.SAURON, Keyword.ENGINE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && game.getGameState().getCurrentSiteBlock() == Block.KING
                && game.getGameState().getCurrentSiteNumber() >= 5 && game.getGameState().getCurrentSiteNumber() <= 9) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
