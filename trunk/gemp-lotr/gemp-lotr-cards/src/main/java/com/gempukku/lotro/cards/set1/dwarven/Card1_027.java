package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.AllyOnCurrentSiteModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Ally � Home 3 � Dwarf
 * Strength: 4
 * Vitality: 2
 * Site: 3
 * Game Text: Maneuver: Exert Thrarin to allow him to participate in archery fire and skirmishes until the regroup
 * phase.
 */
public class Card1_027 extends AbstractAlly {
    public Card1_027() {
        super(1, 3, 4, 2, Race.DWARF, Culture.DWARVEN, "Thrarin", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.MANEUVER, "Exert Thrarin to allow him to participate in archery fire and skirmished until the regroup phase");
            action.addCost(new ExertCharacterEffect(playerId, self));
            action.addEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new AllyOnCurrentSiteModifier(self, Filters.sameCard(self)), Phase.REGROUP));
            return Collections.singletonList(action);
        }

        return null;
    }
}
