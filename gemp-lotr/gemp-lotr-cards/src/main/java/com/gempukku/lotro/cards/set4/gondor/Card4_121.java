package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.LiberateASiteEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. While the fellowship is at site 5T, the site number of each Man in
 * a skirmish is +2. Regroup: Discard this condition and exert a Ring-bound Man to liberate a site.
 */
public class Card4_121 extends AbstractPermanent {
    public Card4_121() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Forests of Ithilien", null, true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new MinionSiteNumberModifier(self, Filters.and(Race.MAN, Filters.inSkirmish),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return game.getGameState().getCurrentSiteNumber() == 5 && game.getGameState().getCurrentSiteBlock() == SitesBlock.TWO_TOWERS;
                            }
                        }, 2));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, Race.MAN, Keyword.RING_BOUND)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.MAN, Keyword.RING_BOUND));
            action.appendEffect(
                    new LiberateASiteEffect(self, playerId, null));
            return Collections.singletonList(action);
        }
        return null;
    }
}
