package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.ProxyingModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Ally • Home 3 • Elf
 * Strength: 5
 * Vitality: 2
 * Site: 3
 * Game Text: While you can spot your site 3, Silinde has the game text of that site.
 */
public class Card1_060 extends AbstractAlly {
    public Card1_060() {
        super(2, 3, 5, 2, Culture.ELVEN, "Silinde", "1_60", true);
        addKeyword(Keyword.ELF);
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
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new ProxyingModifier(self, getFilter(self));
    }

    @Override
    public List<? extends Action> getRequiredIsAboutToActions(LotroGame game, Effect effect, EffectResult effectResult, PhysicalCard self) {
        LotroCardBlueprint copied = getCopied(game, self);
        if (copied != null)
            return copied.getRequiredIsAboutToActions(game, effect, effectResult, self);
        return null;
    }

    @Override
    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        LotroCardBlueprint copied = getCopied(game, self);
        if (copied != null)
            return copied.getRequiredWhenActions(game, effectResult, self);
        return null;
    }

    @Override
    public List<? extends Action> getPlayableIsAboutToActions(String playerId, LotroGame game, Effect effect, EffectResult effectResult, PhysicalCard self) {
        LotroCardBlueprint copied = getCopied(game, self);
        if (copied != null)
            return copied.getPlayableIsAboutToActions(playerId, game, effect, effectResult, self);
        return null;
    }

    @Override
    public List<? extends Action> getPlayableWhenActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        LotroCardBlueprint copied = getCopied(game, self);
        if (copied != null)
            return copied.getPlayableWhenActions(playerId, game, effectResult, self);
        return null;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (self.getZone() == Zone.FREE_SUPPORT) {
            LotroCardBlueprint copied = getCopied(game, self);
            if (copied != null)
                return copied.getPlayablePhaseActions(playerId, game, self);
            return null;
        } else if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            List<Action> actions = new LinkedList<Action>();

            appendPlayAllyActions(actions, game, self);
            appendHealAllyActions(actions, game, self);

            return actions;
        }
        return null;
    }
}
