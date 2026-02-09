package org.example.service;

import org.example.model.Message;
import org.example.model.User;
import org.example.observer.Observable;
import org.example.observer.Signal;
import org.example.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Clasa MessageService care ofera functionalitati pentru gestionarea mesajelor
 */
public class MessageService implements Observable {
    private final MessageRepository messageRepo;

    /**
     * Constructor pentru clasa MessageService
     * @param messageRepo MessageRepository, repository-ul pentru mesaje
     */
    public MessageService(MessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    /**
     * Metoda pentru trimiterea unui mesaj
     * @param from User, utilizatorul care trimite mesajul
     * @param to List<User>, lista de utilizatori care primesc mesajul
     * @param text String, continutul mesajului
     * @return Message, mesajul trimis
     */
    public Message sendMessage(User from, List<User> to, String text) {
        Message msg = new Message();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setMessage(text);
        msg.setDate(LocalDateTime.now());
        msg.setReply(null);

        var result = messageRepo.save(msg);
        notifyObservers(new Signal("NEW_MESSAGE"));
        if (result.isPresent()) {
            throw new RuntimeException("Message could not be saved.");
        }

        return msg;
    }

    /**
     * Metoda pentru trimiterea unui raspuns la un mesaj
     * @param from User, utilizatorul care trimite raspunsul
     * @param to List<User>, lista de utilizatori care primesc raspunsul
     * @param text String, continutul raspunsului
     * @param repliedMessage Message, mesajul la care se raspunde
     * @return Message, mesajul de raspuns trimis
     */
    public Message sendReply(User from, List<User> to, String text, Message repliedMessage) {

        Message msg = new Message();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setMessage(text);
        msg.setDate(LocalDateTime.now());
        msg.setReply(repliedMessage);

        var result = messageRepo.save(msg);

        if (result.isPresent()) {
            throw new RuntimeException("Reply could not be saved.");
        }

        return msg;
    }

    /**
     * Metoda pentru obtinerea tuturor mesajelor
     * @return List<Message>, lista tuturor mesajelor
     */
    public List<Message> getAllMessages() {
        List<Message> list = new ArrayList<>();
        messageRepo.getAll().forEach(list::add);
        return list;
    }

    /**
     * Metoda pentru obtinerea mesajelor trimise de un utilizator
     * @param user User, utilizatorul al carui mesaje trimise se doresc
     * @return List<Message>, lista mesajelor trimise de utilizator
     */
    public List<Message> getMessagesSentBy(User user) {
        List<Message> result = new ArrayList<>();

        for (Message m : messageRepo.getAll()) {
            if (m.getFrom().getId().equals(user.getId())) {
                result.add(m);
            }
        }

        return result;
    }

    /**
     * Metoda pentru obtinerea mesajelor primite de un utilizator
     * @param user User, utilizatorul al carui mesaje primite se doresc
     * @return List<Message>, lista mesajelor primite de utilizator
     */
    public List<Message> getMessagesReceivedBy(User user) {
        List<Message> result = new ArrayList<>();

        for (Message m : messageRepo.getAll()) {
            if (m.getTo().stream().anyMatch(u -> u.getId().equals(user.getId()))) {
                result.add(m);
            }
        }

        return result;
    }

    /**
     * Metoda pentru obtinerea conversatiei dintre doi utilizatori
     * @param a User, primul utilizator
     * @param b User, al doilea utilizator
     * @return List<Message>, lista mesajelor din conversatie
     */
    public List<Message> getConversation(User a, User b) {
        List<Message> conv = new ArrayList<>();

        for (Message m : messageRepo.getAll()) {
            boolean aToB = m.getFrom().getId().equals(a.getId()) &&
                    m.getTo().stream().anyMatch(u -> u.getId().equals(b.getId()));

            boolean bToA = m.getFrom().getId().equals(b.getId()) &&
                    m.getTo().stream().anyMatch(u -> u.getId().equals(a.getId()));

            if (aToB || bToA) {
                conv.add(m);
            }
        }

        conv.sort((x, y) -> x.getDate().compareTo(y.getDate()));
        return conv;
    }

    /**
     * Metoda pentru obtinerea raspunsurilor la un mesaj
     * @param parent Message, mesajul pentru care se doresc raspunsurile
     * @return List<Message>, lista raspunsurilor la mesaj
     */
    public List<Message> getReplies(Message parent) {
        List<Message> result = new ArrayList<>();

        for (Message m : messageRepo.getAll()) {
            if (m.getReply() != null && m.getReply().getId().equals(parent.getId())) {
                result.add(m);
            }
        }

        return result;
    }

    /**
     * Metoda pentru stergerea unui mesaj
     * @param id Long, id-ul mesajului de sters
     * @return boolean, true daca mesajul a fost sters, false altfel
     */
    public boolean deleteMessage(Long id) {
        return messageRepo.delete(id).isPresent();
    }

    /**
     * Metoda pentru actualizarea continutului unui mesaj
     * @param msg Message, mesajul de actualizat
     * @param newText String, noul continut al mesajului
     * @return Message, mesajul actualizat
     */
    public Message updateMessage(Message msg, String newText) {
        msg.setMessage(newText);
        var result = messageRepo.update(msg);
        if (result.isPresent()) {
            throw new RuntimeException("Could not update message.");
        }
        return msg;
    }

    /**
     * Metoda pentru trimiterea unui mesaj cu raspuns
     * @param from User, utilizatorul care trimite mesajul
     * @param to List<User>, lista de utilizatori care primesc mesajul
     * @param text String, continutul mesajului
     * @param replyTo Message, mesajul la care se raspunde
     */
    public void sendMessageWithReply(User from, List<User> to, String text, Message replyTo) {
        Message msg = new Message(
                null,
                from,
                to,
                text,
                LocalDateTime.now(),
                replyTo
        );
        msg.setReply(replyTo);

        messageRepo.save(msg);

        notifyObservers(new Signal("NEW_MESSAGE"));
    }

    /**
     * Metoda pentru obtinerea conversatiei dintre un utilizator si mai multi alti utilizatori
     * @param me User, utilizatorul principal
     * @param others List<User>, lista celorlalti utilizatori
     * @return List<Message>, lista mesajelor din conversatie
     */
    public List<Message> getMultiConversation(User me, List<User> others) {
        Set<Long> ids = new HashSet<>();
        ids.add(me.getId());
        others.forEach(u -> ids.add(u.getId()));

        return messageRepo.getStrictConversation(ids);
    }

}
