package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Merry. Maneuver: Exert Merry twice to exert a minion once for each [ROHAN] companion
 * you spot.
 */
public class Card7_242 extends AbstractAttachableFPPossession {
    public Card7_242() {
        super(1, 2, 0, Culture.ROHAN, PossessionClass.HAND_WEAPON, "Merry's Sword", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Merry");
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game, 2, Filters.name("Merry"))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("Merry")));
            int countSpottable = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Culture.ROHAN, CardType.COMPANION);
            for (int i = 0; i < countSpottable; i++) {
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
