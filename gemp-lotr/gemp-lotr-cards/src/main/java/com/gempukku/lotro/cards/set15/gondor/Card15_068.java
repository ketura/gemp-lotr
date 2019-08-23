package com.gempukku.lotro.cards.set15.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.modifiers.condition.OrCondition;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Cloak
 * Vitality: +1
 * Resistance: +1
 * Game Text: Bearer must be a [GONDOR] Man. While bearer is a ranger or at a forest site, each minion skirmishing
 * bearer is site number +2.
 * Skirmish: Add a threat or exert bearer to make a minion skirmishing a [GONDOR] Man site number +2.
 */
public class Card15_068 extends AbstractAttachableFPPossession {
    public Card15_068() {
        super(1, 0, 1, Culture.GONDOR, PossessionClass.CLOAK, "Ranger's Cloak");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    public int getResistance() {
        return 1;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new MinionSiteNumberModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Filters.hasAttached(self))),
                        new OrCondition(
                                new LocationCondition(Keyword.FOREST),
                                new SpotCondition(Filters.hasAttached(self), Keyword.RANGER)), 2));
        return modifiers;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && (PlayConditions.canAddThreat(game, self, 1)
                || PlayConditions.canExert(self, game, self.getAttachedTo()))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new AddThreatsEffect(playerId, self, 1));
            possibleCosts.add(
                    new ExertCharactersEffect(action, self, self.getAttachedTo()));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.inSkirmishAgainst(Culture.GONDOR, Race.MAN)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new MinionSiteNumberModifier(self, card, null, 2)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
