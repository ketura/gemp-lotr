package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: While you can spot 3 underground sites, each [ORC] Orc gains muster. Shadow: Discard this condition from
 * play and spot an [ORC] minion to replace the fellowship’s current site with an underground site from your
 * adventure deck.
 */
public class Card13_120 extends AbstractPermanent {
    public Card13_120() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.ORC, Zone.SUPPORT, "Unforgiving Depths");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, Filters.and(Culture.ORC, Race.ORC), new SpotCondition(3, CardType.SITE, Keyword.UNDERGROUND), Keyword.MUSTER, 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfDiscard(self, game)
                && PlayConditions.canSpot(game, Culture.ORC, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new PlaySiteEffect(action, playerId, null, game.getGameState().getCurrentSiteNumber(), Keyword.UNDERGROUND));
            return Collections.singletonList(action);
        }
        return null;
    }
}
