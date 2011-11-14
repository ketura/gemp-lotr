package com.gempukku.lotro.cards.modifiers.spotting;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public class SimpleLotroCardBlueprint implements LotroCardBlueprint {
    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return false;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Modifier> getStackedOnModifiers(LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public CardType getCardType() {
        return null;
    }

    @Override
    public Culture getCulture() {
        return null;
    }

    @Override
    public int getKeywordCount(Keyword keyword) {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isAllyAtHome(int siteNumber, Block siteBlock) {
        return false;
    }

    @Override
    public PossessionClass getPossessionClass() {
        return null;
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromStacked(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public CostToEffectAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return null;
    }

    @Override
    public Race getRace() {
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    @Override
    public int getResistance() {
        return 0;
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return 0;
    }

    @Override
    public RequiredTriggerAction getDiscardedFromPlayRequiredTrigger(LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public OptionalTriggerAction getKilledOptionalTrigger(LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public Side getSide() {
        return null;
    }

    @Override
    public Signet getSignet() {
        return null;
    }

    @Override
    public Direction getSiteDirection() {
        return null;
    }

    @Override
    public int getSiteNumber() {
        return 0;
    }

    @Override
    public Block getSiteBlock() {
        return null;
    }

    @Override
    public int getStrength() {
        return 0;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public int getVitality() {
        return 0;
    }

    @Override
    public boolean hasKeyword(Keyword keyword) {
        return false;
    }

    @Override
    public boolean isUnique() {
        return false;
    }
}
