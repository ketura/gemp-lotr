package com.gempukku.lotro.cards.set10.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.CheckTurnLimitEffect;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 [SHIRE] companions. Each time a minion is played, you may exert a Hobbit companion
 * to exert that minion (limit once per turn).
 */
public class Card10_114 extends AbstractPermanent {
    public Card10_114() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, "Shadowplay");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Culture.SHIRE, CardType.COMPANION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.MINION)
                && PlayConditions.canExert(self, game, Race.HOBBIT, CardType.COMPANION)) {
            PlayCardResult playResult = (PlayCardResult) effectResult;
            PhysicalCard playedCard = playResult.getPlayedCard();

            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT, CardType.COMPANION));
            action.appendEffect(
                    new CheckTurnLimitEffect(action, self, 1,
                            new ExertCharactersEffect(action, self, playedCard)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
