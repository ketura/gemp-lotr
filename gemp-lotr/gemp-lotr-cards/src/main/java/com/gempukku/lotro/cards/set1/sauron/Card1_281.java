package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Search. To play, exert a [SAURON] tracker. Plays to your support area. Each time the fellowship moves,
 * the Free Peoples player must exert a companion.
 */
public class Card1_281 extends AbstractLotroCardBlueprint {
    public Card1_281() {
        super(Side.SHADOW, CardType.CONDITION, Culture.SAURON, "Under the Watching Eye");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.canPayForShadowCard(game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.keyword(Keyword.TRACKER), Filters.canExert());
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose SAURON tracker", true, Filters.culture(Culture.SAURON), Filters.keyword(Keyword.TRACKER), Filters.canExert()));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Free Peoples player must exert a companion.");
            action.addEffect(
                    new ChooseAndExertCharacterEffect(action, game.getGameState().getCurrentPlayerId(), "Choose companion", false, Filters.type(CardType.COMPANION), Filters.canExert()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
