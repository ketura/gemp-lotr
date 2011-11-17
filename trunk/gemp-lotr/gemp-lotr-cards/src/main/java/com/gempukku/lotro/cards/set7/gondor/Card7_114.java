package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Pippin. Skirmish: Exert Pippin twice to make him strength +1 for each [GONDOR] companion
 * you spot.
 */
public class Card7_114 extends AbstractAttachableFPPossession {
    public Card7_114() {
        super(1, 2, 0, Culture.GONDOR, PossessionClass.HAND_WEAPON, "Pippin's Sword", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Pippin");
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, 2, Filters.name("Pippin"))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("Pippin")));
            action.appendEffect(
                    new ForEachYouSpotEffect(playerId, Culture.GONDOR, CardType.COMPANION) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                    new StrengthModifier(self, Filters.name("Pippin"), spotCount), Phase.SKIRMISH);
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
