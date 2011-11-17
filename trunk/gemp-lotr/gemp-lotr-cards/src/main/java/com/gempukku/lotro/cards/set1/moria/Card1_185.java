package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 1
 * Site: 4
 * Game Text: When you play this minion, you may discard a [DWARVEN] ally or [DWARVEN] condition.
 */
public class Card1_185 extends AbstractMinion {
    public Card1_185() {
        super(2, 6, 1, 4, Race.ORC, Culture.MORIA, "Goblin Warrior");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose DWARVEN ally or DWARVEN condition", Filters.culture(Culture.DWARVEN), Filters.or(CardType.ALLY, CardType.CONDITION)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard dwarvenCard) {
                            action.appendEffect(new DiscardCardsFromPlayEffect(self, dwarvenCard));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
