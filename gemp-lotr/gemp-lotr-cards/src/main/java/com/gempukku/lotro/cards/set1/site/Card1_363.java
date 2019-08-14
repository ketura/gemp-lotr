package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.PlayersCantPlayPhaseEventsOrPhaseSpecialAbilitiesModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 9
 * Type: Site
 * Site: 9
 * Game Text: River. Shadow: Play up to 3 trackers from your discard pile; end your Shadow phase.
 */
public class Card1_363 extends AbstractSite {
    public Card1_363() {
        super("Tol Brandir", SitesBlock.FELLOWSHIP, 9, 9, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            for (int i = 0; i < 3; i++)
                action.appendEffect(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, Keyword.TRACKER));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new PlayersCantPlayPhaseEventsOrPhaseSpecialAbilitiesModifier(null, Phase.SHADOW)));

            return Collections.singletonList(action);
        }
        return null;
    }
}
