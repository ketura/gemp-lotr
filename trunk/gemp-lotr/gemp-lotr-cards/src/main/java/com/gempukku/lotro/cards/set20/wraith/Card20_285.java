package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.cards.modifiers.CantHealModifier;
import com.gempukku.lotro.cards.modifiers.CantRemoveBurdensModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.GameHasCondition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * Black Breath
 * Ringwraith	Condition • Support Area
 * Skirmish: Transfer this condition from your support area to a character skirmishing a Nazgul.
 * Burdens and wounds may not be removed from bearer.
 */
public class Card20_285 extends AbstractPermanent {
    public Card20_285() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Black Breath");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && self.getZone() == Zone.SUPPORT
                && PlayConditions.isActive(game, Filters.character, Filters.inSkirmishAgainst(Race.NAZGUL))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new TransferPermanentEffect(self, game.getGameState().getSkirmish().getFellowshipCharacter()));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantHealModifier(self, Filters.hasAttached(self)));
        modifiers.add(
                new CantRemoveBurdensModifier(self,
                        new GameHasCondition(Filters.ringBearer, Filters.hasAttached(self)), Filters.any));
        return modifiers;
    }
}
