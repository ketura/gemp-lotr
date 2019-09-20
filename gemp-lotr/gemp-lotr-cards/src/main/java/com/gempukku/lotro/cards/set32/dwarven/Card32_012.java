package com.gempukku.lotro.cards.set32.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Possession
 * Game Text: Bearer must be a Dwarven companion. Fellowship or Regroup: Exert bearer or discard this
 * possession to play the fellowship's next site (replacing opponent's site if necessary). Heal a Dwarven
 * companion if you play a mountain site.
 */
public class Card32_012 extends AbstractAttachableFPPossession {
    public Card32_012() {
        super(0, 0, 0, Culture.DWARVEN, null, "Thror's Map", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.DWARVEN, CardType.COMPANION);
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, PhysicalCard self) {
        if ((PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                || PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self))
                && (PlayConditions.canExert(self, game, Filters.hasAttached(self))
                || PlayConditions.canSelfDiscard(self, game))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.hasAttached(self)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert bearer";
                        }
                    });
            possibleCosts.add(
                    new SelfDiscardEffect(self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard this possession";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new PlaySiteEffect(action, playerId, null, game.getGameState().getCurrentSiteNumber() + 1) {
                        @Override
                        public void sitePlayedCallback(PhysicalCard site) {
                            if (game.getModifiersQuerying().hasKeyword(game, site, Keyword.MOUNTAIN))
                                action.appendEffect(
                                        new ChooseAndHealCharactersEffect(action, playerId, Culture.DWARVEN, CardType.COMPANION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
