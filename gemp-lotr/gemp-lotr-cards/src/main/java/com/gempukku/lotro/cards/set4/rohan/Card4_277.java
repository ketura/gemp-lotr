package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Ally • Home 4T • Man
 * Strength: 4
 * Vitality: 2
 * Site: 4T
 * Game Text: Villager. Discard Guma if an opponent controls his home site. Fellowship: Exert Guma to heal
 * a [ROHAN] Man.
 */
public class Card4_277 extends AbstractAlly {
    public Card4_277() {
        super(1, Block.TWO_TOWERS, 4, 4, 2, Race.MAN, Culture.ROHAN, "Guma", true);
        addKeyword(Keyword.VILLAGER);
    }


    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Filters.sameCard(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Culture.ROHAN, Race.MAN));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (Filters.countActive(game.getGameState(), game.getModifiersQuerying(),
                Filters.siteControlledByShadowPlayer(self.getOwner()),
                Filters.siteNumber(4),
                Filters.siteBlock(Block.TWO_TOWERS)) > 0) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
