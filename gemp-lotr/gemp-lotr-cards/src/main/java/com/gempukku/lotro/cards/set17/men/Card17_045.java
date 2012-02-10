package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.actions.SubCostToEffectAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.StackActionEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Spot a [MEN] possession in your support area and a [MEN] minion. Make that minion strength +1 for each
 * minion stacked on that possession. You may discard that possession to make that minion damage +1.
 */
public class Card17_045 extends AbstractEvent {
    public Card17_045() {
        super(Side.SHADOW, 1, Culture.MEN, "In the Wild Men's Wake", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a MEN possession in your support are", Culture.MEN, CardType.POSSESSION, Zone.SUPPORT) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard possession) {
                        action.appendEffect(
                                new ChooseActiveCardEffect(self, playerId, "Choose a MEN minion", Culture.MEN, CardType.MINION) {
                                    @Override
                                    protected void cardSelected(LotroGame game, PhysicalCard minion) {
                                        action.appendEffect(
                                                new AddUntilEndOfPhaseModifierEffect(
                                                        new StrengthModifier(self, minion, null,
                                                                Filters.filter(game.getGameState().getStackedCards(possession), game.getGameState(), game.getModifiersQuerying(), CardType.MINION).size()), Phase.SKIRMISH));
                                        SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                                        subAction.appendCost(
                                                new DiscardCardsFromPlayEffect(self, possession));
                                        subAction.appendEffect(
                                                new AddUntilEndOfPhaseModifierEffect(
                                                        new KeywordModifier(self, minion, Keyword.DAMAGE, 1), Phase.SKIRMISH));
                                        action.appendEffect(
                                                new OptionalEffect(action, playerId, new StackActionEffect(subAction) {
                                                    @Override
                                                    public String getText(LotroGame game) {
                                                        return "Discard that possession to make that minion damage +1";
                                                    }
                                                }));
                                    }
                                });
                    }
                });
        return action;
    }
}
