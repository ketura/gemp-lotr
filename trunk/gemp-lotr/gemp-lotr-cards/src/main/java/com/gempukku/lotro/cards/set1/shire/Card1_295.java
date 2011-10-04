package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PlaySiteEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally • Home 1 • Hobbit
 * Strength: 2
 * Vitality: 2
 * Site: 1
 * Game Text: While you can spot your site 1, this ally has the game text of that site. Fellowship: Exert this ally and
 * spot opponent's site 1 to replace it with your site 1.
 */
public class Card1_295 extends AbstractAlly {
    public Card1_295() {
        super(1, 1, 2, 2, Race.HOBBIT, Culture.SHIRE, "Hobbit Farmer");
    }

    private Filter getFilter(PhysicalCard self) {
        return Filters.and(Filters.type(CardType.SITE), Filters.owner(self.getOwner()), Filters.siteNumber(1));
    }

    private LotroCardBlueprint getCopied(LotroGame game, PhysicalCard self) {
        PhysicalCard firstActive = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), getFilter(self));
        if (firstActive != null)
            return firstActive.getBlueprint();
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        LotroCardBlueprint copied = getCopied(game, self);
        if (copied != null)
            return copied.getRequiredBeforeTriggers(game, effect, self);
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        LotroCardBlueprint copied = getCopied(game, self);
        if (copied != null)
            return copied.getRequiredAfterTriggers(game, effectResult, self);
        return null;
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        LotroCardBlueprint copied = getCopied(game, self);
        if (copied != null)
            return copied.getOptionalBeforeActions(playerId, game, effect, self);
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        LotroCardBlueprint copied = getCopied(game, self);
        if (copied != null)
            return copied.getOptionalAfterTriggers(playerId, game, effectResult, self);
        return null;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && !game.getGameState().getSite(1).getOwner().equals(playerId)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP);
            action.appendCost(new ExertCharactersEffect(self, self));
            action.appendEffect(new PlaySiteEffect(playerId, 1));
            actions.add(action);
        }
        LotroCardBlueprint copied = getCopied(game, self);
        if (copied != null) {
            List<? extends Action> list = copied.getPhaseActions(playerId, game, self);
            if (list != null)
                actions.addAll(list);
        }
        return actions;
    }

}
