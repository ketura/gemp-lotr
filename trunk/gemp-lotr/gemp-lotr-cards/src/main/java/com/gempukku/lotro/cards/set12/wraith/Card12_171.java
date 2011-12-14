package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Possession • Mount
 * Vitality: +1
 * Game Text: Bearer must be a Nazgul. While bearer is at a battleground or forest site, he is fierce. Assignment: Make
 * the Free Peoples player assign bearer to an unbound companion bearing a [WRAITH] condition.
 */
public class Card12_171 extends AbstractAttachable {
    public Card12_171() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.WRAITH, PossessionClass.MOUNT, "Shadowy Mount");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.NAZGUL;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), new LocationCondition(Filters.or(Keyword.BATTLEGROUND, Keyword.FOREST)), Keyword.FIERCE, 1));
        return modifiers;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canBeAssignedToSkirmishByEffect(self, game, Filters.hasAttached(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, game.getGameState().getCurrentPlayerId(), self.getAttachedTo(), Filters.unboundCompanion, Filters.hasAttached(Culture.WRAITH, CardType.CONDITION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
