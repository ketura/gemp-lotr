package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
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
        super(2, 6, 6, 3, Race.ELF, Culture.ELVEN, "Celeborn", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Exert Celeborn to Heal an ELVEN ally");
            action.addCost(new ExertCharacterEffect(playerId, self));
            action.addEffect(new ChooseActiveCardEffect(playerId, "Choose an ELVEN ally", Filters.culture(Culture.ELVEN), Filters.type(CardType.ALLY)) {
                @Override
                protected void cardSelected(PhysicalCard elvenAlly) {
                    action.addEffect(new HealCharacterEffect(playerId, elvenAlly));
                }
            });

            return Collections.singletonList(action);
        }
        return null;
    }
}
