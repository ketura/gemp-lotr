package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title: Fiery Terrace
 * Set: Second Edition
 * Side: None
 * Site Number: 8
 * Shadow Number: 8
 * Card Number: 1U304
 * Game Text: Battleground. Shadow: Play the Witch-king from your hand or draw deck; his twilight cost is -4.
 */
public class Card40_304 extends AbstractSite {
    public Card40_304() {
        super("Fiery Terrace", Block.SECOND_ED, 8, 8, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && GameUtils.isShadow(game, playerId)
                && (PlayConditions.canPlayFromHand(playerId, game, -4, Filters.witchKing)
                || PlayConditions.canPlayFromDeck(playerId, game, Filters.witchKing))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleEffects = new ArrayList<Effect>(2);
            possibleEffects.add(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, -4, Filters.witchKing));
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, -4, Filters.witchKing));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
