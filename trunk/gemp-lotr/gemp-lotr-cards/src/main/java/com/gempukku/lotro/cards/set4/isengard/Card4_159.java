package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Strength: -1
 * Game Text: Search. To play, exert an [ISENGARD] tracker. Plays on a companion (except the Ring-bearer).
 * Assignment: Exert an [ISENGARD] tracker and remove (2) to assign that tracker to bearer. Bearer may exert
 * to prevent this.
 */
public class Card4_159 extends AbstractAttachable {
    public Card4_159() {
        super(Side.SHADOW, CardType.CONDITION, 1, Culture.ISENGARD, null, "Many Riddles");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canExert(self, game, Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.TRACKER));
    }

    @Override
    public AttachPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        final AttachPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, additionalAttachmentFilter, twilightModifier);
        playCardAction.appendCost(
                new ChooseAndExertCharactersEffect(playCardAction, playerId, 1, 1, Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.TRACKER), Filters.canBeAssignedToSkirmish(Side.SHADOW)));
        return playCardAction;
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER)));
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.hasAttached(self), -1));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.ASSIGNMENT, self, 2)
                && PlayConditions.canExert(self, game, Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.TRACKER), Filters.notAssignedToSkirmish(), Filters.canBeAssignedToSkirmish(Side.SHADOW))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.TRACKER), Filters.notAssignedToSkirmish(), Filters.canBeAssignedToSkirmish(Side.SHADOW)) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard minion) {
                            action.appendEffect(
                                    new PreventableEffect(action,
                                            new AssignmentEffect(playerId, self.getAttachedTo(), Collections.singletonList(minion), "Assigned by card effect"),
                                            game.getGameState().getCurrentPlayerId(),
                                            new ExertCharactersEffect(self, self.getAttachedTo())));
                        }
                    });
            action.appendCost(
                    new RemoveTwilightEffect(2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
