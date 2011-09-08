package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.cards.effects.DiscardCardAtRandomFromHandEffect;
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
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Search. To play, exert a [MORIA] minion. Plays to your support area. Each time the fellowship moves to
 * site 4, 5, or 6 and contains a Dwarf or Elf, the Free Peoples player discards 2 cards at random from hand.
 */
public class Card1_198 extends AbstractLotroCardBlueprint {
    public Card1_198() {
        super(Side.SHADOW, CardType.CONDITION, Culture.MORIA, "Through the Misty Mountains");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.canPayForShadowCard(game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.type(CardType.MINION), Filters.canExert());
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT, twilightModifier);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose MORIA minion to exert", true, Filters.culture(Culture.MORIA), Filters.type(CardType.MINION), Filters.canExert()));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        int siteNumber = game.getGameState().getCurrentSiteNumber();
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO
                && siteNumber >= 4 && siteNumber <= 6
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION), Filters.or(Filters.keyword(Keyword.ELF), Filters.keyword(Keyword.DWARF)))) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Free Peoples player discards 2 cards at random from hand.");
            action.addEffect(new DiscardCardAtRandomFromHandEffect(game.getGameState().getCurrentPlayerId()));
            action.addEffect(new DiscardCardAtRandomFromHandEffect(game.getGameState().getCurrentPlayerId()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
