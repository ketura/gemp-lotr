package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: To play, exert a [ISENGARD] minion. Plays on a site. While the fellowship is at this site, skip the
 * archery phase. Discard this condition at the end of the turn.
 */
public class Card1_140 extends AbstractAttachable {
    public Card1_140() {
        super(Side.SHADOW, CardType.CONDITION, 2, Culture.ISENGARD, null, "Spies of Saruman");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.type(CardType.SITE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION));
    }

    @Override
    public AttachPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        AttachPermanentAction action = super.getPlayCardAction(playerId, game, self, additionalAttachmentFilter, twilightModifier);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION)));
        return action;
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new AbstractModifier(self, "Skip archery phase", null, new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER}) {
            @Override
            public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, String playerId) {
                if (phase == Phase.ARCHERY
                        && gameState.getCurrentSite() == self.getAttachedTo())
                    return true;
                return false;
            }
        };
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.END_OF_TURN) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new DiscardCardsFromPlayEffect(self, self));

            return Collections.singletonList(action);
        }

        return null;
    }
}
