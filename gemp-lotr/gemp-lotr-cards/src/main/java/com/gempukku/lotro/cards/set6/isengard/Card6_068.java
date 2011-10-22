package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardStackedCardsEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 6
 * Type: Minion • Orc
 * Strength: 11
 * Vitality: 4
 * Site: 4
 * Game Text: Regroup: Discard an [ISENGARD] Orc to make the Free Peoples player wound a companion (or 2 companions if
 * you spot 6 companions). Regroup: If this minion is stacked on an [ISENGARD] card, spot an [ISENGARD] Orc and discard
 * this minion to add (3).
 */
public class Card6_068 extends AbstractMinion {
    public Card6_068() {
        super(6, 11, 4, 4, Race.ORC, Culture.ISENGARD, "Isengard Mechanics");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.REGROUP, self, 0)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.ISENGARD, Race.ORC)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.ISENGARD, Race.ORC));
            int count = (Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) >= 6) ? 2 : 1;
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, count, count, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromStacked(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseStackedShadowCardDuringPhase(game.getGameState(), Phase.REGROUP, self, 0)
                && self.getStackedOn().getBlueprint().getCulture() == Culture.ISENGARD
                && PlayConditions.canSpot(game, Culture.ISENGARD, Race.ORC)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardStackedCardsEffect(self, self));
            action.appendEffect(
                    new AddTwilightEffect(self, 3));
            return Collections.singletonList(action);
        }
        return null;
    }
}
