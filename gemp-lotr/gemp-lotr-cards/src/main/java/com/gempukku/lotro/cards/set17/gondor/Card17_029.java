package com.gempukku.lotro.cards.set17.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExhaustCharactersEffect;
import com.gempukku.lotro.cards.modifiers.MinionSiteNumberModifier;
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
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Ranged Weapon
 * Game Text: Bearer must be a [GONDOR] ranger. If bearer is Faramir, he is an archer. The site number of each minion
 * skirmishing a [GONDOR] ranger is +1. Skirmish: Exert bearer twice to exhaust a minion Faramir is skirmishing.
 */
public class Card17_029 extends AbstractAttachableFPPossession {
    public Card17_029() {
        super(1, 0, 0, Culture.GONDOR, PossessionClass.RANGED_WEAPON, "Faramir's Bow", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Keyword.RANGER);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.name("Faramir")), Keyword.ARCHER));
        modifiers.add(
                new MinionSiteNumberModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Culture.GONDOR, Keyword.RANGER)), null, 1));
        return modifiers;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, 2, Filters.hasAttached(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendEffect(
                    new ChooseAndExhaustCharactersEffect(action, playerId, 1, 1, CardType.MINION, Filters.inSkirmishAgainst(Filters.hasAttached(self))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
