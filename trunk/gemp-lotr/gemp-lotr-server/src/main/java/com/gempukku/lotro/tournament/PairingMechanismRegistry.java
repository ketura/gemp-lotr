package com.gempukku.lotro.tournament;

public class PairingMechanismRegistry {
    public PairingMechanism getPairingMechanism(String pairingType) {
        if (pairingType.equals("singleElimination"))
            return new SingleEliminationPairing("singleElimination");
        if (pairingType.equals("swiss"))
            return new SwissPairingMechanism("swiss");
        if (pairingType.equals("swiss-3"))
            return new SwissPairingMechanism("swiss-3", 3);

        return null;
    }
}
