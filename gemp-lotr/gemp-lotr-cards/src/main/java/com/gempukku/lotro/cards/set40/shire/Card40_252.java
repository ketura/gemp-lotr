package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Hamfast Gamgee, The Gaffer
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally - Hobbit - Shire
 * Strength: 2
 * Vitality: 2
 * Card Number: 1R252
 * Game Text: Fellowship: Play a Hobbit ally to heal a Hobbit companion.
 */
public class Card40_252 extends AbstractAlly {
    public Card40_252() {
        super(1, Block.SECOND_ED, 0, 2, 2, Race.HOBBIT, Culture.SHIRE, "Hamfast Gamgee", "The Gaffer", true);
        addKeyword(Keyword.SHIRE);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
        && PlayConditions.canPlayFromHand(playerId, game, Race.HOBBIT, CardType.ALLY)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Race.HOBBIT, CardType.ALLY));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Race.HOBBIT, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
