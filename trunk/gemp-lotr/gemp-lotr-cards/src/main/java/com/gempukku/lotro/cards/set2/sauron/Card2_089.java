package com.gempukku.lotro.cards.set2.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CancelEventEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.PlayEventEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 2
 * Site: 6
 * Game Text: Tracker. The roaming penalty for each [SAURON] minion you play is -1. Response: If an event is played
 * that spots or exerts a ranger, exert or discard this minion to cancel that event.
 */
public class Card2_089 extends AbstractMinion {
    public Card2_089() {
        super(2, 6, 2, 6, Race.ORC, Culture.SAURON, "Orc Scout");
        addKeyword(Keyword.TRACKER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new RoamingPenaltyModifier(self, Filters.and(Filters.culture(Culture.SAURON), Filters.type(CardType.MINION)), -1));
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effect, Filters.type(CardType.EVENT))) {
            PlayEventEffect playEffect = (PlayEventEffect) effect;
            if (playEffect.isRequiresRanger()) {
                ActivateCardAction action = new ActivateCardAction(self, Keyword.RESPONSE);
                List<Effect> possibleCosts = new LinkedList<Effect>();
                possibleCosts.add(
                        new ExertCharactersEffect(self, self));
                possibleCosts.add(
                        new DiscardCardsFromPlayEffect(self));
                action.appendCost(
                        new ChoiceEffect(action, playerId, possibleCosts));
                action.appendEffect(
                        new CancelEventEffect(playerId, playEffect));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
