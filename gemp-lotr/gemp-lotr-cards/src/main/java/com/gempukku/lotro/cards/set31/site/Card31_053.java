package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
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
 * Set: The Short Rest
 * Twilight Cost: 6
 * Type: Site
 * Site: 7
 * Game Text: River. Regroup: Spot your minion and 6 companions to make The Fellowship move again this turn
 * (if the move limit allows).
 */
public class Card31_053 extends AbstractSite {
    public Card31_053() {
        super("Dale", SitesBlock.HOBBIT, 7, 6, Direction.RIGHT);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSpot(game, Filters.owner(playerId), CardType.MINION)
                && PlayConditions.canSpot(game, 6, CardType.COMPANION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new SpecialFlagModifier(self, ModifierFlag.HAS_TO_MOVE_IF_POSSIBLE)));
            return Collections.singletonList(action);
        }
        return null;
    }
}