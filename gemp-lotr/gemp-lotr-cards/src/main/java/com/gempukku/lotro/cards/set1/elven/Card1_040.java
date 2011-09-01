package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 4
 * Type: Ally � Home 3 � Elf
 * Strength: 8
 * Vitality: 4
 * Site: 3
 * Game Text: To play, spot Gandalf or an Elf. At the start of each of your turns, heal every ally whose home is site 3.
 * Fellowship: Exert Elrond to draw a card.
 */
public class Card1_040 extends AbstractAlly {
    public Card1_040() {
        super(4, 3, 8, 4, Culture.ELVEN, "Elrond", true);
        addKeyword(Keyword.ELF);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.keyword(Keyword.ELF), Filters.name("Gandalf"))))
            appendPlayAllyActions(actions, game, self);

        appendHealAllyActions(actions, game, self);

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            CostToEffectAction action = new CostToEffectAction(self, Keyword.FELLOWSHIP, "Exert Elrond to draw a card");
            action.addCost(new ExertCharacterEffect(self));
            action.addEffect(new DrawCardEffect(playerId));
            actions.add(action);
        }

        return actions;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            List<PhysicalCard> allies = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.ALLY), Filters.siteNumber(3));

            CostToEffectAction action = new CostToEffectAction(self, null, "Heal every ally whose home is site 3");
            for (PhysicalCard ally : allies)
                action.addEffect(new HealCharacterEffect(ally));

            return Collections.singletonList(action);
        }
        return null;
    }
}
