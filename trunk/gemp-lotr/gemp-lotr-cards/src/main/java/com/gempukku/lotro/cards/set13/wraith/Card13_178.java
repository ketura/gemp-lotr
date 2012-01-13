package com.gempukku.lotro.cards.set13.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.actions.SubCostToEffectAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.StackActionEffect;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 3
 * Type: Event • Regroup
 * Game Text: To play, exert your Nazgul twice and spot an exhausted companion. Heal that companion to add a burden.
 * If the fellowship is not in region 1, you may repeat this for each other wound on that companion.
 */
public class Card13_178 extends AbstractEvent {
    public Card13_178() {
        super(Side.SHADOW, 3, Culture.WRAITH, "Dark Fell About Him", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, 2, Filters.owner(playerId), Race.NAZGUL)
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.exhausted);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose an exhausted companion", CardType.COMPANION, Filters.exhausted) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard companion) {
                        action.appendCost(
                                new HealCharactersEffect(self, companion));
                        action.appendEffect(
                                new AddBurdenEffect(self, 1));

                        if (!PlayConditions.location(game, Filters.region(1))) {
                            int wounds = game.getGameState().getWounds(companion) - 1;
                            for (int i = 0; i < wounds; i++) {
                                SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                                subAction.appendCost(
                                        new HealCharactersEffect(self, companion));
                                subAction.appendEffect(
                                        new AddBurdenEffect(self, 1));
                                action.appendEffect(
                                        new OptionalEffect(action, playerId,
                                                new StackActionEffect(subAction) {
                                                    @Override
                                                    public String getText(LotroGame game) {
                                                        return "Heal " + GameUtils.getCardLink(companion) + " to add a burden";
                                                    }
                                                }));
                            }
                        }
                    }
                });
        return action;
    }
}
