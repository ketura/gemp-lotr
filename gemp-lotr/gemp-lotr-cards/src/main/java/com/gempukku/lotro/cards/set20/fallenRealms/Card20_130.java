package com.gempukku.lotro.cards.set20.fallenRealms;


import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.ExtraFilters;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 5
 * Mumak Chieftan
 * Fallen Realms	Minion • Man
 * 11	3	4
 * Southron.
 * When you play this minion, you may play a [FR] mount on him from your discard pile at twilight cost -2
 */
public class Card20_130 extends AbstractMinion {
    public Card20_130() {
        super(5, 11, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Mumak Chieftain");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, -2, PossessionClass.MOUNT, Culture.FALLEN_REALMS, ExtraFilters.attachableTo(game, -2, self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDiscard(playerId), Filters.and(PossessionClass.MOUNT, Culture.FALLEN_REALMS, ExtraFilters.attachableTo(game, -2, self)), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                game.getActionsEnvironment().addActionToStack(PlayUtils.getPlayCardAction(game, selectedCard, -2, self, false));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
