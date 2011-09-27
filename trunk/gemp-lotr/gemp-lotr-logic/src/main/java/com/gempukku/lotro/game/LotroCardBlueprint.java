package com.gempukku.lotro.game;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public interface LotroCardBlueprint {
    public Side getSide();

    public CardType getCardType();

    public Culture getCulture();

    public Race getRace();

    public boolean isUnique();

    public String getName();

    public Signet getSignet();

    public boolean hasKeyword(Keyword keyword);

    public int getKeywordCount(Keyword keyword);

    public int getTwilightCost();

    public int getStrength();

    public int getVitality();

    public int getResistance();

    public List<Modifier> getAlwaysOnModifiers(PhysicalCard self);

    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier);

    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier);

    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self);

    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self);

    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self);

    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self);

    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self);

    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self);

    public List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self);

    public int getSiteNumber();

    public Direction getSiteDirection();

    public enum Direction {
        LEFT, RIGHT
    }
}
