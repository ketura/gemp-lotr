package com.gempukku.lotro.cards.set32.esgaroth;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Esgaroth
 * Twilight Cost: 3
 * Type: Ally • Home 6 • Man
 * Strength: 6
 * Vitality: 4
 * Site: 6
 * Game Text: Archer. Maneuver: Spot Bard and exert Esgaroth Volunteers to allow Esgaroth Volunteers to
 * participate in archery fire and skirmishes until the regroup phase.
 */
public class Card32_017 extends AbstractAlly {
    public Card32_017() {
        super(3, SitesBlock.HOBBIT, 6, 6, 4, Race.MAN, Culture.ESGAROTH, "Esgaroth Volunteers", null, true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game, self)
                && Filters.canSpot(game, Filters.name("Bard"))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new AllyParticipatesInArcheryFireAndSkirmishesModifier(self, self), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
