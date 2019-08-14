package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 10
 * Type: Minion • Troll
 * Strength: 14
 * Vitality: 4
 * Site: 5
 * Game Text: Besieger. Fierce. To play, spot a [SAURON] Orc. The twilight cost of Troll of Gorgoroth is -2 for each
 * [SAURON] engine you spot. Regroup: Exert Troll of Gorgoroth to stack your besieger on a site you control.
 */
public class Card8_108 extends AbstractMinion {
    public Card8_108() {
        super(10, 14, 4, 5, Race.TROLL, Culture.SAURON, "Troll of Gorgoroth", "Abomination of Sauron", true);
        addKeyword(Keyword.BESIEGER);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.SAURON, Race.ORC);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return -2 * Filters.countActive(game, Culture.SAURON, Keyword.ENGINE);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a site you control", Filters.siteControlled(playerId)) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard site) {
                            action.insertEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose your Besieger", Filters.owner(playerId), Keyword.BESIEGER) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard besieger) {
                                            action.insertEffect(
                                                    new StackCardFromPlayEffect(besieger, site));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
