package com.gempukku.lotro.cards.set18.uruk_hai;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Uruk-Hai
 * Twilight Cost: 4
 * Type: Event • Skirmish
 * Game Text: Spot your Uruk-hai skirmishing a companion to use vitality to resolve that skirmish instead of strength.
 */
public class Card18_115 extends AbstractEvent {
    public Card18_115() {
        super(Side.SHADOW, 4, Culture.URUK_HAI, "Final Triumph", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.owner(self.getOwner()), Race.URUK_HAI, Filters.inSkirmishAgainst(CardType.COMPANION));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        final Skirmish skirmish = game.getGameState().getSkirmish();
                        if (skirmish != null) {
                            Evaluator vitalityEvaluator = new Evaluator() {
                                @Override
                                public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                    return game.getModifiersQuerying().getVitality(game, cardAffected);
                                }
                            };

                            skirmish.setFpStrengthOverrideEvaluator(vitalityEvaluator);
                            skirmish.setShadowStrengthOverrideEvaluator(vitalityEvaluator);
                        }
                    }
                });
        return action;
    }
}
