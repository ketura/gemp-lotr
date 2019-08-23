package com.gempukku.lotro.cards.set31.gundabad;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 4
 * Type: Possession • Mount
 * Strength: +4
 * Vitality: +2
 * Game Text: Bearer must a [GUNDABAD] Orc. Bearer is fierce. Assignment: Exert bearer twice or remove 2
 * doubts to assign bearer to Thorin.
 */
public class Card31_026 extends AbstractAttachable {
    public Card31_026() {
        super(Side.SHADOW, CardType.POSSESSION, 4, Culture.GUNDABAD, PossessionClass.MOUNT, "Threatening Warg");
    }

    @Override
    public int getStrength() {
        return 4;
    }

    @Override
    public int getVitality() {
        return 2;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.FIERCE));
        return modifiers;
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.WARG_RIDER;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && (PlayConditions.canExert(self, game, 2, self.getAttachedTo())
                || game.getGameState().getBurdens() >= 2)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, self.getAttachedTo()) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert bearer twice";
                        }
                    });
            possibleCosts.add(
                    new RemoveBurdenEffect(playerId, self, 2) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove two doubts";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, playerId, self.getAttachedTo(), Filters.name("Thorin")));
            return Collections.singletonList(action);
        }
        return null;
    }
}
