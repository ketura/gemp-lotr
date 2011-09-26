package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
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
        super(2, 6, 3, 3, Race.ELF, Culture.ELVEN, "Orophin", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.ARCHERY, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.ARCHERY, "Exert to wound an Uruk-hai");
            action.appendCost(new ExertCharactersCost(playerId, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai", Filters.race(Race.URUK_HAI)) {
                        @Override
                        protected void cardSelected(PhysicalCard urukHai) {
                            action.appendEffect(new CardAffectsCardEffect(self, urukHai));
                            action.appendEffect(new WoundCharacterEffect(playerId, urukHai));
                        }
                    });
            return Collections.singletonList(action);
        }

        return null;
    }
}
