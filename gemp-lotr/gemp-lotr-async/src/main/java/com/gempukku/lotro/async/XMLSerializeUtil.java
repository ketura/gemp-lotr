package com.gempukku.lotro.async;

import com.gempukku.lotro.async.handler.ChatRequestHandler;
import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.game.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class XMLSerializeUtil {
    public static Document serializeChatRoomData(PlayerDAO playerDAO, String room,
                                             Collection<ChatMessage> chatMessages, Collection<String> usersInRoom) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

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

        Set<String> users = new TreeSet<>(new CaseInsensitiveStringComparator());
        for (String userInRoom : usersInRoom)
            users.add(formatPlayerNameForChatList(playerDAO, userInRoom));

        for (String userInRoom : users) {
            Element user = doc.createElement("user");
            user.appendChild(doc.createTextNode(userInRoom));
            chatElem.appendChild(user);
        }

        return doc;
    }

    private static class CaseInsensitiveStringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    }

    private static String formatPlayerNameForChatList(PlayerDAO playerDao, String userInRoom) {
        final Player player = playerDao.getPlayer(userInRoom);
        if (player != null) {
            final String playerType = player.getType();
            if (playerType.contains("a"))
                return "* " + userInRoom;
            else if (playerType.contains("l"))
                return "+ " + userInRoom;
        }
        return userInRoom;
    }
}
