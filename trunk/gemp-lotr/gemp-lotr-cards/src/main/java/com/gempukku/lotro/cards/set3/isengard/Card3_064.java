package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.cards.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 6
 * Type: Minion • Orc
 * Strength: 11
 * Vitality: 4
 * Site: 4
 * Game Text: Each other [ISENGARD] Orc is strength +1. Regroup: Exert Orc Commander to make the Free Peoples player
 * wound a companion.
 */
public class Card3_064 extends AbstractMinion {
    public Card3_064() {
        super(6, 11, 4, 4, Race.ORC, Culture.ISENGARD, "Orc Commander", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self,
                        Filters.and(
                                Filters.not(Filters.sameCard(self)),
                                Filters.culture(Culture.ISENGARD),
                                Filters.race(Race.ORC)
                        ), 1));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.REGROUP, self, 0)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.REGROUP);
            action.appendCost(
                    new ExertCharactersCost(self, self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, Filters.type(CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
