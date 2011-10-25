package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.*;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Strength: -1
 * Game Text: Spell. To play, exert Saruman or an [ISENGARD] Man. Plays on a Free Peoples Man. Special abilities
 * in bearer's game text may not be used.
 */
public class Card4_157 extends AbstractAttachable {
    public Card4_157() {
        super(Side.SHADOW, CardType.CONDITION, 0, Culture.ISENGARD, null, "Leechraft");
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canExert(self, game, Filters.or(Filters.name("Saruman"), Filters.and(Filters.culture(Culture.ISENGARD), Filters.race(Race.MAN))));
    }

    @Override
    public AttachPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        final AttachPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, additionalAttachmentFilter, twilightModifier);
        playCardAction.appendCost(
                new ChooseAndExertCharactersEffect(playCardAction, playerId, 1, 1, Filters.or(Filters.name("Saruman"), Filters.and(Filters.culture(Culture.ISENGARD), Filters.race(Race.MAN)))));
        return playCardAction;
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.side(Side.FREE_PEOPLE), Filters.race(Race.MAN));
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), -1));
        modifiers.add(
                new AbstractModifier(self, null, null, ModifierEffect.ACTION_MODIFIER) {
                    @Override
                    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
                        if (action.getActionSource() == self.getAttachedTo())
                            return false;
                        return true;
                    }
                });
        return modifiers;
    }
}
