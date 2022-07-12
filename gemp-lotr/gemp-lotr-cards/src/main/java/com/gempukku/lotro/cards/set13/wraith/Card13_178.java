package com.gempukku.lotro.cards.set13.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, 2, Filters.owner(self.getOwner()), Race.NAZGUL)
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.exhausted);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.owner(playerId), Race.NAZGUL));
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose an exhausted companion", CardType.COMPANION, Filters.exhausted) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard companion) {
                        action.appendCost(
                                new HealCharactersEffect(self, self.getOwner(), companion));
                        action.appendEffect(
                                new AddBurdenEffect(self.getOwner(), self, 1));

                        if (!PlayConditions.location(game, Filters.region(1))) {
                            int wounds = game.getGameState().getWounds(companion) - 1;
                            for (int i = 0; i < wounds; i++) {
                                SubAction subAction = new SubAction(action);
                                subAction.appendCost(
                                        new HealCharactersEffect(self, self.getOwner(), companion));
                                subAction.appendEffect(
                                        new AddBurdenEffect(self.getOwner(), self, 1));
                                action.appendEffect(
                                        new OptionalEffect(action, playerId,
                                                new StackActionEffect(subAction) {
                                                    @Override
                                                    public String getText(LotroGame game) {
                                                        return "Heal " + GameUtils.getFullName(companion) + " to add a burden";
                                                    }
                                                }));
                            }
                        }
                    }
                });
        return action;
    }
}
