package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 2
 * Site: 6
 * Game Text: When you play a weapon on this minion, add (2) (limit once per turn).
 */
public class Card1_266 extends AbstractMinion {
    public Card1_266() {
        super(2, 7, 2, 6, Race.ORC, Culture.SAURON, "Orc Chieftain");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.PLAY) {
            PlayCardResult playCardResult = (PlayCardResult) effectResult;
            PhysicalCard playedCard = playCardResult.getPlayedCard();
            if (playCardResult.getAttachedTo() == self
                    && self.getData() == null
                    && Filters.weapon.accepts(game.getGameState(), game.getModifiersQuerying(), playedCard)
                    ) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new AddTwilightEffect(self, 2));
                action.appendEffect(
                        new UnrespondableEffect() {
                            @Override
                            public void doPlayEffect(LotroGame game) {
                                self.storeData(new Object());
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        if (effectResult.getType() == EffectResult.Type.END_OF_TURN)
            self.removeData();
        return null;
    }


}
