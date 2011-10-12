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
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 4
 * Type: Ally • Home 3 • Elf
 * Strength: 8
 * Vitality: 4
 * Site: 3
 * Game Text: At the start of each of your turns, you may spot an ally whose home is site 3 to heal that ally twice.
 * Regroup: Exert Elrond twice to heal a companion.
 */
public class Card3_013 extends AbstractAlly {
    public Card3_013() {
        super(4, Block.FELLOWSHIP, 3, 8, 4, Race.ELF, Culture.ELVEN, "Elrond", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an ally", Filters.type(CardType.ALLY), Filters.siteNumber(3), Filters.siteBlock(Block.FELLOWSHIP)) {
                        @Override
                        protected void cardSelected(PhysicalCard ally) {
                            action.insertEffect(
                                    new HealCharactersEffect(playerId, ally));
                            action.insertEffect(
                                    new HealCharactersEffect(playerId, ally));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), 2, Filters.sameCard(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Filters.type(CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
