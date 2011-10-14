package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
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
 * Game Text: Spell. Weather. To play, exert a [ISENGARD] minion. Plays on a site. Limit 1 per site. Each Hobbit at
 * this site is strength -2. Discard this condition at the end of the turn.
 */
public class Card1_135 extends AbstractAttachable {
    public Card1_135() {
        super(Side.SHADOW, CardType.CONDITION, 2, Culture.ISENGARD, null, "Saruman's Frost");
        addKeyword(Keyword.SPELL);
        addKeyword(Keyword.WEATHER);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.type(CardType.SITE), Filters.not(Filters.hasAttached(Filters.name("Saruman's Frost"))));
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
        return new StrengthModifier(self,
                Filters.and(
                        Filters.race(Race.HOBBIT),
                        Filters.type(CardType.COMPANION),
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return gameState.getCurrentSite() == self.getAttachedTo();
                            }
                        }
                ), -2);
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
