package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a [ROHAN] Man. If bearer is Eomer and he is unwounded, he is damage +1 and resistance +1.
 * Skirmish: If bearer is Eomer, discard this possession to wound a minion he is skirmishing.
 */
public class Card15_124 extends AbstractAttachableFPPossession {
    public Card15_124() {
        super(1, 2, 0, Culture.ROHAN, PossessionClass.HAND_WEAPON, "Eomer's Spear", "Trusty Weapon", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.name(Names.eomer), Filters.unwounded), Keyword.DAMAGE, 1));
        modifiers.add(
                new ResistanceModifier(self, Filters.and(Filters.hasAttached(self), Filters.name(Names.eomer), Filters.unwounded), 1));
        return modifiers;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, Filters.hasAttached(self), Filters.name(Names.eomer))
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION, Filters.inSkirmishAgainst(self.getAttachedTo())));
            return Collections.singletonList(action);
        }
        return null;
    }
}
