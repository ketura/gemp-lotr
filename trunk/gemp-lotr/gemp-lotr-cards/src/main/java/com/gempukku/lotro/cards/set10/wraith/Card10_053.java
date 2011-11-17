package com.gempukku.lotro.cards.set10.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Shadow: Exert a Nazgul and play a [WRAITH] minion to add (1) (or (2) if you have initiative).
 */
public class Card10_053 extends AbstractPermanent {
    public Card10_053() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Black Marshal");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canExert(self, game, Race.NAZGUL)
                && PlayConditions.canPlayFromHand(playerId, game, Culture.WRAITH, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.NAZGUL));
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.WRAITH, CardType.MINION));
            int count = PlayConditions.hasInitiative(game, Side.SHADOW) ? 2 : 1;
            action.appendEffect(
                    new AddTwilightEffect(self, count));
            return Collections.singletonList(action);
        }
        return null;
    }
}
