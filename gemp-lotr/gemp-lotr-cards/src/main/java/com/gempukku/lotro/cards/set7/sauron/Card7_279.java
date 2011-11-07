package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 6
 * Type: Minion • Orc
 * Strength: 14
 * Vitality: 4
 * Site: 5
 * Game Text: Besieger. Skirmish: Stack your besieger on a site you control to make Gorgoroth Troop strength +2.
 */
public class Card7_279 extends AbstractMinion {
    public Card7_279() {
        super(6, 14, 4, 5, Race.ORC, Culture.SAURON, "Gorgoroth Troop", true);
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSpot(game, Filters.siteControlled(playerId))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose besieger", Keyword.BESIEGER) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard minion) {
                            action.insertCost(
                                    new ChooseActiveCardEffect(self, playerId, "Choose site you control", Filters.siteControlled(playerId)) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard site) {
                                            action.insertCost(
                                                    new StackCardFromPlayEffect(minion, site));
                                        }
                                    });
                        }
                    });
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 2), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
