package com.gempukku.lotro.server;

import com.gempukku.lotro.collection.DeliveryService;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.game.Player;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public abstract class AbstractResource {
    protected final boolean _test = System.getProperty("test") != null;

    @Context
    protected PlayerDAO _playerDao;

    @Context
    protected DeliveryService _deliveryService;

    protected final void processDeliveryServiceNotification(HttpServletRequest request, HttpServletResponse response) {
        String logged = getLoggedUser(request);
        if (logged != null && _deliveryService.hasUndeliveredPackages(logged))
            response.addHeader("Delivery-Service-Package", "true");
    }

    protected final String getLoggedUser(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("logged");
    }

    protected final Player getResourceOwnerSafely(HttpServletRequest request, String participantId) {
        String loggedUser = getLoggedUser(request);
        if (_test && loggedUser == null)
            loggedUser = participantId;

        if (loggedUser == null)
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);

        Player resourceOwner = _playerDao.getPlayer(loggedUser);

        if (resourceOwner == null)
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);

        if (resourceOwner.getType().equals("a") && participantId != null && !participantId.equals("null")) {
            resourceOwner = _playerDao.getPlayer(participantId);
            if (resourceOwner == null)
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return resourceOwner;
    }

    protected final void sendError(Response.Status status) throws WebApplicationException {
        throw new WebApplicationException(status);
    }
}
