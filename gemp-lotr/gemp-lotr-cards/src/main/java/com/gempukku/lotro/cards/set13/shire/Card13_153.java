package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Artifact • Armor
 * Game Text: Bearer must be a Ring-bound Hobbit. Each minion skirmishing bearer loses all damage bonuses.
 * Skirmish: Exert bearer to make each minion he is skirmishing lose fierce and unable to gain fierce until
 * the regroup phase.
 */
public class Card13_153 extends AbstractAttachableFPPossession {
    public Card13_153() {
        super(2, 0, 0, Culture.SHIRE, CardType.ARTIFACT, PossessionClass.ARMOR, "Mithril-coat", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Race.HOBBIT, Keyword.RING_BOUND);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new RemoveKeywordModifier(self, Filters.inSkirmishAgainst(Filters.hasAttached(self)), Keyword.DAMAGE));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Filters.hasAttached(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.hasAttached(self)));
            final Collection<PhysicalCard> skirmishingAgainst = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmishAgainst(Filters.hasAttached(self)));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new RemoveKeywordModifier(self, Filters.in(skirmishingAgainst), Keyword.FIERCE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
