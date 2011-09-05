package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Signet: Gandalf
 * Game Text: Ring-bearer (resistance 10). At the start of each of your turns, you may heal a Hobbit ally.
 */
public class Card1_289 extends AbstractCompanion {
    public Card1_289() {
        super(0, 3, 4, Culture.SHIRE, "Frodo", true);
        setSignet(Signet.GANDALF);
        addKeyword(Keyword.HOBBIT);
        addKeyword(Keyword.RING_BEARER);
    }

    @Override
    public int getResistance() {
        return 10;
    }

    @Override
    public List<? extends Action> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "You may heal a Hobbit ally");
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a Hobbit ally", Filters.type(CardType.ALLY), Filters.keyword(Keyword.HOBBIT)) {
                        @Override
                        protected void cardSelected(PhysicalCard hobbitAlly) {
                            action.addEffect(new HealCharacterEffect(hobbitAlly));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
