package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays on a site you control. Regroup: Spot 2 [DUNLAND] Men and discard this condition to make
 * the Free Peoples player choose to move again this turn (if the move limit allows).
 */
public class Card4_030 extends AbstractAttachable {
    public Card4_030() {
        super(Side.SHADOW, CardType.CONDITION, 0, Culture.DUNLAND, null, "No Retreat");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.siteControlled(playerId);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Culture.DUNLAND, Race.MAN) >= 2) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new SpecialFlagModifier(self, ModifierFlag.HAS_TO_MOVE_IF_POSSIBLE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
