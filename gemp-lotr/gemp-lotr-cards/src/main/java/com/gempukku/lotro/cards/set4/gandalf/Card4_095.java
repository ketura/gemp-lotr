package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.PlayEventEffect;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Spell.
 * Response: If you play a [GANDALF] event, exert Gandalf twice to place that event in your hand instead of your
 * discard pile.
 */
public class Card4_095 extends AbstractResponseEvent {
    public Card4_095() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Into Dark Tunnels");
        addKeyword(Keyword.SPELL);
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, final Effect effect, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)
                && PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effect, Filters.and(Filters.culture(Culture.GANDALF), Filters.type(CardType.EVENT)))
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), 2, Filters.name("Gandalf"))) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("Gandalf")));
            action.appendEffect(
                    new AbstractSuccessfulEffect() {
                        @Override
                        public String getText(LotroGame game) {
                            return null;
                        }

                        @Override
                        public EffectResult.Type getType() {
                            return null;
                        }

                        @Override
                        public EffectResult[] playEffect(LotroGame game) {
                            ((PlayEventEffect) effect).setTargetZone(Zone.HAND);
                            return null;
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
