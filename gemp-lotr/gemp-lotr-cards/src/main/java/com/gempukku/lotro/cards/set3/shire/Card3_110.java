package com.gempukku.lotro.cards.set3.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally • Home 1 • Hobbit
 * Strength: 2
 * Vitality: 2
 * Site: 1
 * Game Text: Response: If a burden is about to be added by a Shadow card, exert Melilot Brandybuck to prevent
 * that burden.
 */
public class Card3_110 extends AbstractAlly {
    public Card3_110() {
        super(1, 1, 2, 2, Race.HOBBIT, Culture.SHIRE, "Melilot Brandybuck", true);
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.ADD_BURDEN
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            final AddBurdenEffect addBurdenEffect = (AddBurdenEffect) effect;
            if (addBurdenEffect.getSource().getBlueprint().getSide() == Side.SHADOW) {
                ActivateCardAction action = new ActivateCardAction(self, Keyword.RESPONSE);
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendEffect(
                        new UnrespondableEffect() {
                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                addBurdenEffect.prevent();
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
