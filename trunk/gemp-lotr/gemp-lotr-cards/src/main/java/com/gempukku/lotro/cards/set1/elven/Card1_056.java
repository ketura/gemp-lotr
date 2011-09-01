package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Ally • Home 6 • Elf
 * Strength: 3
 * Vitality: 3
 * Site: 6
 * Game Text: Archer. Archery: Exert Orophin to wound an Uruk-hai.
 */
public class Card1_056 extends AbstractAlly {
    public Card1_056() {
        super(2, 6, 3, 3, Culture.ELVEN, "Orophin", true);
        addKeyword(Keyword.ELF);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        appendPlayAllyActions(actions, game, self);
        appendHealAllyActions(actions, game, self);

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.ARCHERY, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.ARCHERY, "Exert to wound an Uruk-hai");
            action.addCost(new ExertCharacterEffect(self));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai", Filters.keyword(Keyword.URUK_HAI)) {
                        @Override
                        protected void cardSelected(PhysicalCard urukHai) {
                            action.addEffect(new WoundCharacterEffect(urukHai));
                        }
                    });
            actions.add(action);
        }

        return actions;
    }
}
