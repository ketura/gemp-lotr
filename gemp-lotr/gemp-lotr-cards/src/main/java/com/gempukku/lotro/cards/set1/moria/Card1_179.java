package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 1
 * Site: 4
 * Game Text: When you play this minion, you may play a weapon from your discard pile on your [MORIA] Orc.
 */
public class Card1_179 extends AbstractMinion {
    public Card1_179() {
        super(3, 8, 1, 4, Keyword.ORC, Culture.MORIA, "Goblin Scavengers");
    }

    @Override
    public List<? extends Action> getOptionalAfterTriggers(final String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            final Filter additionalAttachmentFilter = Filters.and(Filters.owner(self.getOwner()), Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ORC));

            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Play a weapon from your discard pile on your [MORIA] Orc.");
            action.addEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDiscard(playerId),
                            Filters.and(
                                    Filters.or(Filters.keyword(Keyword.HAND_WEAPON), Filters.keyword(Keyword.RANGED_WEAPON)),
                                    new Filter() {
                                        @Override
                                        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                            AbstractAttachable weapon = (AbstractAttachable) physicalCard.getBlueprint();
                                            return weapon.checkPlayRequirements(playerId, game, physicalCard, additionalAttachmentFilter, 0);
                                        }
                                    })
                            , 1, 1) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            PhysicalCard selectedCard = selectedCards.get(0);
                            game.getActionsEnvironment().addActionToStack(((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, additionalAttachmentFilter, 0));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}


