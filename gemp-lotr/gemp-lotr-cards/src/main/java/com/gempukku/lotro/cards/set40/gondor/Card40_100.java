package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Blade of Gondor, Weapon of Honor
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession - Hand Weapon
 * Strength: +2
 * Card Number: 1R100
 * Game Text: Bearer must be Boromir. He is damage +1.
 * Skirmish: Exert Boromir to wound an Orc or Uruk-hai skirmishing another unbound companion.
 */
public class Card40_100 extends AbstractAttachableFPPossession {
    public Card40_100() {
        super(1, 2, 0, Culture.GONDOR, PossessionClass.HAND_WEAPON, "Blade of Gondor", "Weapon of Honor", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.boromir;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 1);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Filters.boromir)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.boromir));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1,
                            Filters.or(Race.ORC, Race.URUK_HAI), Filters.inSkirmishAgainst(Filters.not(Filters.hasAttached(self)), Filters.unboundCompanion)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
