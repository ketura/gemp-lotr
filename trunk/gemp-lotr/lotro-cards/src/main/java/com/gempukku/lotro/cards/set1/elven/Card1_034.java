package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.HealCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Ally � Home 6 � Elf
 * Strength: 6
 * Vitality: 3
 * Site: 6
 * Game Text: Fellowship: Exert Celeborn to heal an [ELVEN] ally.
 */
public class Card1_034 extends AbstractAlly {
    public Card1_034() {
        super(2, 6, 6, 3, Culture.ELVEN, "Celeborn", "1_34", true);
        addKeyword(Keyword.ELF);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        appendPlayAllyActions(actions, game, self);
        appendHealAllyActions(actions, game, self);

        if (game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP
                && self.getZone() == Zone.FREE_SUPPORT) {
            final CostToEffectAction action = new CostToEffectAction(self, "Exert Celeborn to Heal an ELVEN ally");
            action.addCost(new ExertCharacterEffect(self));
            action.addEffect(new ChooseActiveCardEffect(playerId, "Choose an ELVEN ally", Filters.culture(Culture.ELVEN), Filters.type(CardType.ALLY)) {
                @Override
                protected void cardSelected(LotroGame game, PhysicalCard elvenAlly) {
                    action.addEffect(new HealCardEffect(elvenAlly));
                }
            });

            actions.add(action);
        }

        return actions;
    }
}
