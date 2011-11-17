package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Ally • Home 6 • Elf
 * Strength: 3
 * Vitality: 3
 * Site: 6
 * Game Text: At the start of each of your turns, you may heal an Elf. Fellowship: Exert Galadriel to play
 * the fellowship's next site if it is a forest (replacing opponent's site if necessary).
 */
public class Card3_017 extends AbstractAlly {
    public Card3_017() {
        super(3, Block.FELLOWSHIP, 6, 3, 3, Race.ELF, Culture.ELVEN, "Galadriel", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Race.ELF));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            Collection<PhysicalCard> nextSites = Filters.filter(game.getGameState().getAdventureDeck(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.siteNumber(game.getGameState().getCurrentSiteNumber() + 1));
            if (nextSites.size() > 0 && game.getModifiersQuerying().hasKeyword(game.getGameState(), nextSites.iterator().next(), Keyword.FOREST)) {
                action.appendEffect(
                        new PlaySiteEffect(playerId, null, game.getGameState().getCurrentSiteNumber() + 1));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
