package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier;
import com.gempukku.lotro.logic.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * 0
 * Easterling Broadshield
 * Fallen Realms	Possession • Shield
 * 1
 * Bearer must be an Easterling.
 * The fellowship archery total is -1.
 * While you can spot 6 companions, the Free Peoples player may not play archery events or use archery special abilities.
 */
public class Card20_111 extends AbstractAttachable {
    public Card20_111() {
        super(Side.SHADOW, CardType.POSSESSION, 0, Culture.FALLEN_REALMS, PossessionClass.SHIELD, "Easterling Broadshield");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.EASTERLING;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new ArcheryTotalModifier(self, Side.FREE_PEOPLE, -1));
        modifiers.add(
                new SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(self,
                        new SpotCondition(6, CardType.COMPANION), Side.FREE_PEOPLE, Phase.ARCHERY));
        return modifiers;
    }
}
