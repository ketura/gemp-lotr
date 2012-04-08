package com.gempukku.lotro.server;

import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.chat.ChatRoomMediator;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.game.Player;
import com.sun.jersey.spi.resource.Singleton;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Collection;
import java.util.List;

@Singleton
@Path("/chat")
public class ChatResource extends AbstractResource {
    @Context
    private ChatServer _chatServer;

    @Path("/{room}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getMessages(
            @PathParam("room") String room,
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        ChatRoomMediator chatRoom = _chatServer.getChatRoom(room);
        if (chatRoom == null)
            sendError(Response.Status.NOT_FOUND);

        List<ChatMessage> chatMessages = chatRoom.joinUser(resourceOwner.getName());
        Collection<String> usersInRoom = chatRoom.getUsersInRoom();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        serializeChatRoomData(room, chatMessages, usersInRoom, doc);

        return doc;
    }

    @Path("/{room}")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document postMessage(
            @PathParam("room") String room,
            @FormParam("participantId") String participantId,
            @FormParam("message") List<String> messages,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        ChatRoomMediator chatRoom = _chatServer.getChatRoom(room);
        if (chatRoom == null)
            sendError(Response.Status.NOT_FOUND);

        if (messages != null) {
            for (String message : messages) {
                if (message != null && message.trim().length() > 0)
                    chatRoom.sendMessage(resourceOwner.getName(), StringEscapeUtils.escapeHtml(message));
            }
        }

        List<ChatMessage> chatMessages = chatRoom.getPendingMessages(resourceOwner.getName());

        Collection<String> usersInRoom = chatRoom.getUsersInRoom();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        serializeChatRoomData(room, chatMessages, usersInRoom, doc);

        return doc;
    }

    private void serializeChatRoomData(String room, List<ChatMessage> chatMessages, Collection<String> usersInRoom, Document doc) {
        Element chatElem = doc.createElement("chat");
        chatElem.setAttribute("roomName", room);
        doc.appendChild(chatElem);

        for (ChatMessage chatMessage : chatMessages) {
            Element message = doc.createElement("message");
            message.setAttribute("from", chatMessage.getFrom());
            message.setAttribute("date", String.valueOf(chatMessage.getWhen().getTime()));
            message.appendChild(doc.createTextNode(chatMessage.getMessage()));
            chatElem.appendChild(message);
        }

        for (String userInRoom : usersInRoom) {
            Element user = doc.createElement("user");
            user.appendChild(doc.createTextNode(userInRoom));
            chatElem.appendChild(user);
        }
    }
}
