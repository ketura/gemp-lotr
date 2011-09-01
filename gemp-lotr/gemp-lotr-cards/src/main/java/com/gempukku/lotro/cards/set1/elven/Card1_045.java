package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
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
        super(3, 6, 3, 3, Culture.ELVEN, "Galadriel", true);
        addKeyword(Keyword.ELF);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        appendPlayAllyActions(actions, game, self);
        appendHealAllyActions(actions, game, self);

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            CostToEffectAction action = new CostToEffectAction(self, Keyword.FELLOWSHIP, "Exert Galadriel to play an Elf for free");
            action.addCost(new ExertCharacterEffect(self));
            action.addEffect(
                    new ChooseCardsFromHandEffect(playerId, "Choose an Elf to play", 1, 1, Filters.keyword(Keyword.ELF),
                            new Filter() {
                                @Override
                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                    List<? extends Action> playableActions = physicalCard.getBlueprint().getPhaseActions(playerId, game, physicalCard);
                                    return (playableActions != null && playableActions.size() > 0);
                                }
                            }) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            LotroCardBlueprint blueprint = selectedCards.get(0).getBlueprint();
                            if (!blueprint.isUnique() || !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name(blueprint.getName()))) {
                                Zone zone = (blueprint.getCardType() == CardType.COMPANION) ? Zone.FREE_CHARACTERS : Zone.FREE_SUPPORT;
                                game.getActionsEnvironment().addActionToStack(new PlayPermanentAction(selectedCards.get(0), zone, -1000));
                            }
                        }
                    });
            actions.add(action);
        }

        return actions;
    }


    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            List<PhysicalCard> allies = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.ALLY), Filters.siteNumber(6));

            CostToEffectAction action = new CostToEffectAction(self, null, "Heal every ally whose home is site 6");
            for (PhysicalCard ally : allies)
                action.addEffect(new HealCharacterEffect(ally));

            return Collections.singletonList(action);
        }
        return null;
    }

}
