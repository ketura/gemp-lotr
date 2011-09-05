package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert an Elf to discard a [SAURON] minion, a [SAURON] condition, or a [SAURON] possession.
 */
public class Card1_063 extends AbstractLotroCardBlueprint {
    public Card1_063() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.ELVEN, "Stand Against Darkness");
        addKeyword(Keyword.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.MANEUVER, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF), Filters.canExert())) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.MANEUVER, "Exert and Elf to discard a SAURON minion, a SAURON condition, or a SAURON possession");
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose an Elf", Filters.keyword(Keyword.ELF), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard elf) {
                            action.addCost(new ExertCharacterEffect(elf));
                        }
                    });
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose SAURON minion, condition or possession", Filters.culture(Culture.SAURON),
                            Filters.or(Filters.type(CardType.MINION), Filters.type(CardType.CONDITION), Filters.type(CardType.POSSESSION))) {
                        @Override
                        protected void cardSelected(PhysicalCard sauronCard) {
                            action.addEffect(new DiscardCardFromPlayEffect(sauronCard));
                        }
                    }
            );

            return Collections.singletonList(action);
        }
        return null;
    }
}
