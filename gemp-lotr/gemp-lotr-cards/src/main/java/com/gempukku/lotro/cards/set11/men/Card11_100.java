package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 2
 * Site: 4
 * Game Text: Maneuver: Exert this minion to transfer a possession or condition borne by a character to another
 * eligible bearer.
 */
public class Card11_100 extends AbstractMinion {
    public Card11_100() {
        super(4, 10, 2, 4, Race.MAN, Culture.MEN, "Strange-looking Men");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a possession or condition", Filters.or(CardType.POSSESSION, CardType.CONDITION), Filters.attachedTo(Filters.character),
                            new Filter() {
                                @Override
                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                    if (!(physicalCard.getBlueprint() instanceof AbstractAttachable))
                                        return false;

                                    AbstractAttachable attachable = (AbstractAttachable) physicalCard.getBlueprint();
                                    return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), attachable.getFullValidTargetFilter(physicalCard.getOwner(), game, physicalCard), Filters.not(physicalCard.getAttachedTo()));
                                }
                            }) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard attachment) {
                            AbstractAttachable attachable = (AbstractAttachable) attachment.getBlueprint();
                            action.insertEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose new eligible bearer", attachable.getFullValidTargetFilter(attachment.getOwner(), game, attachment), Filters.not(attachment.getAttachedTo())) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard newBearer) {
                                            action.insertEffect(
                                                    new TransferPermanentEffect(attachment, newBearer));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
