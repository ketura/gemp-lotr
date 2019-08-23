package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title: Bow of the North
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession - Ranged Weapon
 * Strength: +1
 * Card Number: 1C105
 * Game Text: Bearer must be a [GONDOR] Man.
 * Skirmish: Exert bearer to wound a roaming minion he is skirmishing or exert bearer to wound any minion he
 * is skirmishing if at a site from your adventure deck.
 */
public class Card40_105 extends AbstractAttachableFPPossession {
    public Card40_105() {
        super(1, 1, 0, Culture.GONDOR, PossessionClass.RANGED_WEAPON, "Bow of the North");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Filters.hasAttached(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.hasAttached(self)));
            List<Effect> possibleEffects = new ArrayList<Effect>(2);
            possibleEffects.add(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.roamingMinion, Filters.inSkirmishAgainst(Filters.hasAttached(self))) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Wound a roaming minion in skirmish against bearer";
                        }
                    });
            if (PlayConditions.location(game, Filters.owner(playerId)))
                possibleEffects.add(
                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.inSkirmishAgainst(Filters.hasAttached(self))) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Wound any minion in skirmish against bearer";
                            }
                        });
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
