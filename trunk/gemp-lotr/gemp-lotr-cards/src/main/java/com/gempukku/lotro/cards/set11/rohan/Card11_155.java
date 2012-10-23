package com.gempukku.lotro.cards.set11.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, exert a [ROHAN] Man. While you can spot 2 [ROHAN] mounts, the move limit is +1. Each of your
 * companions that is not mounted is strength -1.
 */
public class Card11_155 extends AbstractPermanent {
    public Card11_155() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Riding Like the Wind");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Culture.ROHAN, Race.MAN);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        playCardAction.appendCost(
                new ChooseAndExertCharactersEffect(playCardAction, playerId, 1, 1, Culture.ROHAN, Race.MAN));
        return playCardAction;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(self).getUsedLimit() < 1
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), 2, Culture.ROHAN, PossessionClass.MOUNT)) {
            game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                    new MoveLimitModifier(self,1));
            game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(self).incrementToLimit(1, 1);
        }
        return null;

    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(Filters.owner(self.getOwner()), CardType.COMPANION, Filters.not(Filters.mounted)), -1));
        return modifiers;
    }
}
