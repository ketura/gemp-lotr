package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 1
 * Type: Minion • Man
 * Strength: 5
 * Vitality: 1
 * Site: 3
 * Game Text: While skirmishing a [ROHAN] Man, this minion is strength +2. Assignment: Spot an ally to make that ally
 * participate in skirmishes and assign this minion to skirmish that ally.
 */
public class Card4_015 extends AbstractMinion {
    public Card4_015() {
        super(1, 5, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Ravager");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self,
                        Filters.and(
                                Filters.sameCard(self),
                                Filters.inSkirmishAgainst(
                                        Filters.and(
                                                Filters.culture(Culture.ROHAN),
                                                Filters.race(Race.MAN)))), 2));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.ASSIGNMENT, self, 0)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.ALLY), Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, playerId, self, CardType.ALLY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
