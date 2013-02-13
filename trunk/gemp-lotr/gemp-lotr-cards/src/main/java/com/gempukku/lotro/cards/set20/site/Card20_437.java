package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Moria Caverns
 * 4	4
 * Underground.
 * Regroup: Exert a goblin and spot an unbound hobbit to make the free people's player move again this turn
 */
public class Card20_437 extends AbstractSite {
    public Card20_437() {
        super("Moria Caverns", Block.SECOND_ED, 4, 4, null);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, Filters.owner(playerId), Race.GOBLIN)
                && PlayConditions.canSpot(game, Filters.unboundCompanion, Race.HOBBIT)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.owner(playerId), Race.GOBLIN));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new SpecialFlagModifier(self, ModifierFlag.HAS_TO_MOVE_IF_POSSIBLE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
