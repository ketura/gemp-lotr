package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.modifiers.ProxyingModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Ally • Home 6 • Elf
 * Strength: 5
 * Vitality: 2
 * Site: 6
 * Game Text: While you can spot your site 6, Uruviel has the game text of that site.
 */
public class Card1_067 extends AbstractAlly {
    public Card1_067() {
        super(2, 6, 5, 2, Race.ELF, Culture.ELVEN, "Uruviel", true);
    }

    private Filter getFilter(PhysicalCard self) {
        return Filters.and(Filters.type(CardType.SITE), Filters.owner(self.getOwner()), Filters.siteNumber(3));
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
        LotroCardBlueprint copied = getCopied(game, self);
        if (copied != null)
            return copied.getPhaseActions(playerId, game, self);
        return null;
    }
}
