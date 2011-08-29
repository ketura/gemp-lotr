package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.HealCardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Bearer must be a Dwarf. When you play this condition, heal bearer up to 2 times. At the start of each of
 * your turns, exert bearer.
 */
public class Card1_010 extends AbstractAttachable {
    public Card1_010() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 0, Culture.DWARVEN, "Dwarven Heart", "1_10");
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        Filter validTargetFilter = Filters.keyword(Keyword.DWARF);

        appendAttachCardFromHandAction(actions, game, self, validTargetFilter);

        return actions;
    }

    @Override
    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            CostToEffectAction action = new CostToEffectAction(self, "Heal bearer up to 2 times");
            action.addEffect(new HealCardEffect(self.getAttachedTo()));
            action.addEffect(new HealCardEffect(self.getAttachedTo()));
            return Collections.<Action>singletonList(action);
        } else if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            CostToEffectAction action = new CostToEffectAction(self, "Exert bearer at the start of each of your turns");
            action.addEffect(new ExertCharacterEffect(self.getAttachedTo()));
            return Collections.<Action>singletonList(action);
        }

        return null;
    }
}
