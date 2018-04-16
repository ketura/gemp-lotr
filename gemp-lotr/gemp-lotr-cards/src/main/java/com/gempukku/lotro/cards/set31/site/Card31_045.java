package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Twilight Cost: 2
 * Type: Site
 * Site: 2
 * Game Text: Forest. At the start of the maneuver phase, each player may play a hand weapon from his or her draw deck.
 */
public class Card31_045 extends AbstractSite {
    public Card31_045() {
        super("Trollshaw Forest", Block.HOBBIT, 2, 2, Direction.RIGHT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            for (String playerId : GameUtils.getAllPlayers(game)) {
                action.appendEffect(new OptionalEffect(action, playerId,
                    new ChooseAndPlayCardFromDeckEffect(playerId, PossessionClass.HAND_WEAPON)));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
