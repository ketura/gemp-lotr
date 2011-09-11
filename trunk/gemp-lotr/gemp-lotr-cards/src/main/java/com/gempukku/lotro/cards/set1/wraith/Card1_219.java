package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Search. Plays to your support area. Each time you play a Nazgul, you may exert a Hobbit (except
 * the Ring-bearer).
 */
public class Card1_219 extends AbstractPermanent {
    public Card1_219() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SHADOW_SUPPORT, "The Nine Servants of Sauron");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public List<? extends Action> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.owner(self.getOwner()), Filters.race(Race.NAZGUL)))) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Exert a Hobbit (except the Ring-Bearer)");
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a Hobbit (except the Ring-Bearer)", Filters.race(Race.HOBBIT), Filters.not(Filters.keyword(Keyword.RING_BEARER))) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            action.addEffect(
                                    new ExertCharacterEffect(card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
