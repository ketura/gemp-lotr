package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
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
        super(3, 6, 3, 3, Race.ELF, Culture.ELVEN, "Galadriel", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Exert Galadriel to play an Elf for free");
            action.addCost(new ExertCharacterEffect(self));
            action.addEffect(
                    new ChooseCardsFromHandEffect(playerId, "Choose an Elf to play", 1, 1, Filters.race(Race.ELF), Filters.playable(game, -1000)) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            PhysicalCard selectedCard = selectedCards.get(0);
                            game.getActionsEnvironment().addActionToStack(
                                    selectedCard.getBlueprint().getPlayCardAction(playerId, game, selectedCard, -1000));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            List<PhysicalCard> allies = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.ALLY), Filters.siteNumber(6));

            RequiredTriggerAction action = new RequiredTriggerAction(self, null, "Heal every ally whose home is site 6");
            for (PhysicalCard ally : allies)
                action.addEffect(new HealCharacterEffect(ally));

            return Collections.singletonList(action);
        }
        return null;
    }

}
