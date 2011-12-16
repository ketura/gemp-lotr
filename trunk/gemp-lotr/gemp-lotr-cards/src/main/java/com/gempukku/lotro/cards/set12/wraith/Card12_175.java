package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.discount.ToilDiscountEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 11
 * Vitality: 4
 * Site: 3
 * Game Text: Fierce.Toil 1. (For each [WRAITH] character you exert when playing this, its twilight cost is -1)
 * Response: If you are playing a [WRAITH] event that has toil X, exert Úlairë Enquëa to reduce that event's twilight
 * cost by X.
 */
public class Card12_175 extends AbstractMinion {
    public Card12_175() {
        super(6, 11, 4, 3, Race.NAZGUL, Culture.WRAITH, "Úlairë Enquëa", true);
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == Effect.Type.BEFORE_TOIL
                && PlayConditions.canSelfExert(self, game)) {
            final ToilDiscountEffect toilEffect = (ToilDiscountEffect) effect;
            PhysicalCard payingFor = toilEffect.getPayingFor();
            if (Filters.and(Filters.owner(playerId), Culture.WRAITH, CardType.EVENT).accepts(game.getGameState(), game.getModifiersQuerying(), payingFor)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new SelfExertEffect(self));
                action.appendEffect(
                        new UnrespondableEffect() {
                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                toilEffect.incrementToil();
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
