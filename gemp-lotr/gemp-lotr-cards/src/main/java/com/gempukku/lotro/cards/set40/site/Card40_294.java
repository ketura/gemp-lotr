package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
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
 * Title: Tower of Orthanc
 * Set: Second Edition
 * Side: None
 * Site Number: 5
 * Shadow Number: 6
 * Card Number: 1U294
 * Game Text: Battleground. Shadow: Play Saruman from your hand, draw deck, or discard pile; his twilight cost is -2.
 */
public class Card40_294 extends AbstractSite {
    public Card40_294() {
        super("Tower of Orthanc", Block.SECOND_ED, 5, 6, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && GameUtils.isShadow(game, playerId)
                && (PlayConditions.canPlayFromHand(playerId, game, -2, Filters.saruman)
                || PlayConditions.canPlayFromDeck(playerId, game, Filters.saruman)
                || PlayConditions.canPlayFromDiscard(playerId, game, -2, Filters.saruman))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleEffects = new ArrayList<Effect>(3);
            possibleEffects.add(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, -2, Filters.saruman));
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, -2, Filters.saruman));
            possibleEffects.add(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, -2, Filters.saruman));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
