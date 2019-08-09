package com.gempukku.lotro.cards.set17.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.PreventCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: To play, spot Theoden (or 2 [ROHAN] companions). Saruman and Grima cannot be assigned to skirmishes.
 * Response: If a [ROHAN] Man is about to take a wound, exert another [ROHAN] Man and add (2) to prevent that.
 */
public class Card17_098 extends AbstractPermanent {
    public Card17_098() {
        super(Side.FREE_PEOPLE, 2, CardType.ARTIFACT, Culture.ROHAN, Zone.SUPPORT, "Throne of the Golden Hall", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && (PlayConditions.canSpot(game, Filters.name(Names.theoden))
                || PlayConditions.canSpot(game, 2, Culture.ROHAN, CardType.COMPANION));
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new CantBeAssignedToSkirmishModifier(self, Filters.or(Filters.saruman, Filters.name("Grima")));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.ROHAN, Race.MAN)
                && PlayConditions.canExert(self, game, Culture.ROHAN, Race.MAN)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            Collection<PhysicalCard> woundedCharacters = Filters.filter(woundEffect.getAffectedCardsMinusPrevented(game), game, Culture.ROHAN, Race.MAN);
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
            for (PhysicalCard woundedCharacter : woundedCharacters) {
                if (PlayConditions.canExert(self, game, Culture.ROHAN, Race.MAN, Filters.not(woundedCharacter))) {
                    ActivateCardAction action = new ActivateCardAction(self);
                    action.setText("Prevent to " + GameUtils.getFullName(woundedCharacter));
                    action.appendCost(
                            new AddTwilightEffect(self, 2));
                    action.appendCost(
                            new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ROHAN, Race.MAN, Filters.not(woundedCharacter)));
                    action.appendEffect(
                            new PreventCardEffect(woundEffect, woundedCharacter));
                    actions.add(action);
                
                }
            }
            return actions;
        }
        return null;
    }
}
