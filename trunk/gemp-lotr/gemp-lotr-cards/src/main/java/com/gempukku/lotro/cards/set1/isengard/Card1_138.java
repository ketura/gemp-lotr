package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
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
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Spell. Weather. To play, exert a [ISENGARD] minion. Plays on a site. No player may play skirmish events
 * or use skirmish special abilities during skirmishes at this site. Discard this condition at the end of the turn.
 */
public class Card1_138 extends AbstractAttachable {
    public Card1_138() {
        super(Side.SHADOW, CardType.CONDITION, 2, Culture.ISENGARD, null, "Saruman's Snows");
        addKeyword(Keyword.SPELL);
        addKeyword(Keyword.WEATHER);
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
        return new AbstractModifier(self, "Can't play Skirmish actions", null, new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER}) {
            @Override
            public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, Action action, boolean result) {
                PhysicalCard actionSource = action.getActionSource();
                if ((action.getType() == Keyword.SKIRMISH
                        || (actionSource != null && actionSource.getBlueprint().getCardType() == CardType.EVENT && modifiersQuerying.hasKeyword(gameState, actionSource, Keyword.SKIRMISH)))
                        && gameState.getCurrentSite() == self.getAttachedTo()) {
                    return false;
                }
                return result;
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
