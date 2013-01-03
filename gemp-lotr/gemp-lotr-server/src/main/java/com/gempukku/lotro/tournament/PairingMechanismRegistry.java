package com.gempukku.lotro.tournament;

public class PairingMechanismRegistry {
    public PairingMechanism getPairingMechanism(String pairingType) {
        if (pairingType.equals("singleElimination"))
            return new SingleEliminationPairing("singleElimination");
        if (pairingType.equals("swiss"))
            return new SwissPairingMechanism("swiss");

        return null;
    }
}
