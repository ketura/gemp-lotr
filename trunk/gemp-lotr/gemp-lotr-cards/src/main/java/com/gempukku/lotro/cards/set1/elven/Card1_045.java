package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentFromHandAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.HealCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseAnyCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Ally � Home 6 � Elf
 * Strength: 3
 * Vitality: 3
 * Site: 6
 * Game Text: At the start of each of your turns, heal every ally whose home is site 6. Fellowship: Exert Galadriel to
 * play an Elf for free.
 */
public class Card1_045 extends AbstractAlly {
    public Card1_045() {
        super(3, 6, 3, 3, Culture.ELVEN, "Galadriel", "1_45", true);
        addKeyword(Keyword.ELF);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, final LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        appendPlayAllyActions(actions, game, self);
        appendHealAllyActions(actions, game, self);

        if (game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP
                && self.getZone() == Zone.FREE_SUPPORT
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            CostToEffectAction action = new CostToEffectAction(self, "Exert Galadriel to play an Elf for free");
            action.addCost(new ExertCharacterEffect(self));
            action.addEffect(
                    new ChooseAnyCardEffect(playerId, "Choose an Elf to play", Filters.owner(playerId), Filters.zone(Zone.HAND), Filters.keyword(Keyword.ELF)) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            LotroCardBlueprint blueprint = card.getBlueprint();
                            if (!blueprint.isUnique() || !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name(blueprint.getName()))) {
                                Zone zone = (blueprint.getCardType() == CardType.COMPANION) ? Zone.FREE_CHARACTERS : Zone.FREE_SUPPORT;
                                game.getActionsEnvironment().addActionToStack(new PlayPermanentFromHandAction(card, zone, -1000));
                            }
                        }
                    });
            actions.add(action);
        }

        return actions;
    }


    @Override
    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            List<PhysicalCard> allies = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.ALLY), Filters.siteNumber(6));

            CostToEffectAction action = new CostToEffectAction(self, "Heal every ally whose home is site 6");
            for (PhysicalCard ally : allies)
                action.addEffect(new HealCardEffect(ally));

            return Collections.singletonList(action);
        }
        return null;
    }

}
