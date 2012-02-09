package com.gempukku.lotro.cards.set17.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveFromTheGameCardsInDiscardEffect;
import com.gempukku.lotro.cards.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.CanSpotCultureTokensCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 4
 * Type: Condition • Support Area
 * Game Text: When you play this condition, you may add a [DWARVEN] token here. While you can spot 4 [DWARVEN] tokens
 * and 2 Dwarves, each [ORC] Orc gains this text 'To play, remove an [ORC] card from your discard pile from the game.'
 * Skirmish: Discard this condition to exert a minion for each Dwarf you can spot.
 */
public class Card17_002 extends AbstractPermanent {
    public Card17_002() {
        super(Side.FREE_PEOPLE, 4, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Balin Avenged", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.DWARVEN));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new AbstractExtraPlayCostModifier(self, "To play, remove an ORC card from your discard pile from the game.", Filters.and(Culture.ORC, Race.ORC),
                new AndCondition(
                        new CanSpotCultureTokensCondition(4, Token.DWARVEN),
                        new SpotCondition(2, Race.DWARF))) {
            @Override
            public boolean canPayExtraCostsToPlay(GameState gameState, ModifiersQuerying modifiersQueirying, PhysicalCard card) {
                return Filters.filter(gameState.getDiscard(card.getOwner()), gameState, modifiersQueirying, Culture.ORC).size() > 0;
            }

            @Override
            public List<? extends Effect> getExtraCostsToPlay(GameState gameState, ModifiersQuerying modifiersQueirying, Action action, PhysicalCard card) {
                return Collections.singletonList(
                        new ChooseAndRemoveFromTheGameCardsInDiscardEffect(action, self, self.getOwner(), 1, 1, Culture.ORC));
            }
        };
    }
}
