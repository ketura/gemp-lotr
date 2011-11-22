package com.gempukku.lotro.cards.set8.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
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
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +3
 * Game Text: Bearer must be a Nazgul. Skirmish: If bearer is The Witch-king, exert him and spot a possession borne by
 * a character he is skirmishing to discard that possession.
 */
public class Card8_070 extends AbstractAttachable {
    public Card8_070() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.WRAITH, PossessionClass.HAND_WEAPON, "Black Flail", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.hasAttached(self), 3));
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.NAZGUL;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && self.getAttachedTo().getBlueprint().getName().equals("The Witch-king")
                && PlayConditions.canExert(self, game, Filters.hasAttached(self))
                && PlayConditions.canSpot(game, CardType.POSSESSION, Filters.attachedTo(Filters.inSkirmishAgainst(Filters.hasAttached(self))))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Filters.attachedTo(Filters.inSkirmishAgainst(Filters.hasAttached(self)))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
