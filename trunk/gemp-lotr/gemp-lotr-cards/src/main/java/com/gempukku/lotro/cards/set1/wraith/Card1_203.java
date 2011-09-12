package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a Nazgul is about to take a wound, prevent that wound.
 */
public class Card1_203 extends AbstractResponseEvent {
    public Card1_203() {
        super(Side.SHADOW, Culture.WRAITH, "All Blades Perish");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, final Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND) {
            if (((WoundCharacterEffect) effect).getWoundedCard().getBlueprint().getRace() == Race.NAZGUL
                    && PlayConditions.canPayForShadowCard(game, self, 0)) {
                PlayEventAction action = new PlayEventAction(self);
                action.addEffect(
                        new UnrespondableEffect() {
                            @Override
                            public void doPlayEffect(LotroGame game) {
                                ((WoundCharacterEffect) effect).prevent();
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
