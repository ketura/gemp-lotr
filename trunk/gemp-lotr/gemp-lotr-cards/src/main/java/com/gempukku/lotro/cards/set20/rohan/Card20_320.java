package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Eowyn, Noblewoman of Rohan
 * Companion • Man
 * 6	3	7
 * While you can spot a [Rohan] Man, Eowyn's twilight cost is -1.
 * Skirmish: Discard a [Rohan] fortification to heal a companion.
 * http://lotrtcg.org/coreset/rohan/eowynnor(r2).jpg
 */
public class Card20_320 extends AbstractCompanion {
    public Card20_320() {
        super(2, 6, 3, 7, Culture.ROHAN, Race.MAN, null, Names.eowyn, "Noblewoman of Rohan", true);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return Filters.canSpot(gameState, modifiersQuerying, Culture.ROHAN, Race.MAN)?-1:0;
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.ROHAN, Keyword.FORTIFICATION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.ROHAN, Keyword.FORTIFICATION));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
