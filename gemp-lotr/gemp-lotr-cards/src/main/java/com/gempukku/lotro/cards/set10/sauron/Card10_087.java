package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 12
 * Vitality: 3
 * Site: 5
 * Game Text: Besieger. When you play this minion, you may spot 6 companions and another besieger to play a besieger
 * from your discard pile and control a site.
 */
public class Card10_087 extends AbstractMinion {
    public Card10_087() {
        super(5, 12, 3, 5, Race.ORC, Culture.SAURON, "Gorgoroth Swarm");
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, 6, CardType.COMPANION)
                && PlayConditions.canSpot(game, Filters.not(self), Keyword.BESIEGER)
                && PlayConditions.canPlayFromDiscard(playerId, game, Keyword.BESIEGER)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Keyword.BESIEGER));
            action.appendEffect(
                    new TakeControlOfASiteEffect(self, playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
