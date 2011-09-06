package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 7
 * Vitality: 2
 * Site: 5
 * Game Text: Response: If the Ring-bearer puts on The One Ring, exert this minion. Discard a card from the top of your
 * draw deck for each [ISENGARD] minion you spot. Add a burden for each Shadow card discarded in this way.
 */
public class Card1_155 extends AbstractMinion {
    public Card1_155() {
        super(2, 7, 2, 5, Keyword.URUK_HAI, Culture.ISENGARD, "Uruk Spy");
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.PUT_ON_THE_ONE_RING
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.RESPONSE, "Exert this minion. Discard a card from the top of your draw deck for each ISENGARD minion you spot. Add a burden for each Shadow card discarded in this way.");
            action.addCost(new ExertCharacterEffect(self));
            int isengardMinionCount = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION));
            for (int i = 0; i < isengardMinionCount; i++) {
                action.addEffect(
                        new UnrespondableEffect() {
                            @Override
                            public void playEffect(LotroGame game) {
                                List<? extends PhysicalCard> deck = game.getGameState().getDeck(playerId);
                                if (deck.size() > 0 && deck.get(0).getBlueprint().getSide() == Side.SHADOW)
                                    action.addEffect(new AddBurdenEffect(game.getGameState().getCurrentPlayerId()));
                            }
                        });
                action.addEffect(new DiscardTopCardFromDeckEffect(playerId));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
