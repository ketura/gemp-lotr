package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Besieger. Archery: Exert this minion or stack it on a site you control to make the fellowship archery
 * total -2.
 */
public class Card8_097 extends AbstractMinion {
    public Card8_097() {
        super(3, 9, 2, 5, Race.ORC, Culture.SAURON, "Gorgoroth Breaker");
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)
                && (PlayConditions.canSelfExert(self, game) || PlayConditions.canSpot(game, Filters.siteControlled(playerId)))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ExertCharactersEffect(self, self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert this minion";
                        }
                    });
            possibleCosts.add(
                    new ChooseActiveCardEffect(self, playerId, "Choose a site you control", Filters.siteControlled(playerId)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Stack this minion on a site you control";
                        }

                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertCost(
                                    new StackCardFromPlayEffect(self, card));
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, -2), Phase.ARCHERY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
