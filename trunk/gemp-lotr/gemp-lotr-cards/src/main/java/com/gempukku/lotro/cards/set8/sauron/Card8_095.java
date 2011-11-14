package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 2
 * Site: 5
 * Game Text: Besieger. Assignment: Spot a [SAURON] engine or a site you control to assign this minion to an unbound
 * companion.
 */
public class Card8_095 extends AbstractMinion {
    public Card8_095() {
        super(2, 7, 2, 5, Race.ORC, Culture.SAURON, "Gorgoroth Assassin");
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSpot(game, Filters.or(Filters.and(Culture.SAURON, Keyword.ENGINE), Filters.siteControlled(playerId)))
                && Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW).accepts(game.getGameState(), game.getModifiersQuerying(), self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, playerId, self, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
