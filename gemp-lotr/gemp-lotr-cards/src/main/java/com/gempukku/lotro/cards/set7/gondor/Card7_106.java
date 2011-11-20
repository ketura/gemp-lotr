package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Knight. Fellowship: Discard a fortification to heal Ingold twice.
 */
public class Card7_106 extends AbstractCompanion {
    public Card7_106() {
        super(2, 6, 3, 6, Culture.GONDOR, Race.MAN, null, "Ingold", true);
        addKeyword(Keyword.KNIGHT);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canDiscardFromPlay(self, game, Keyword.FORTIFICATION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Keyword.FORTIFICATION));
            action.appendEffect(
                    new HealCharactersEffect(self, self));
            action.appendEffect(
                    new HealCharactersEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
