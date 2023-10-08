package com.example.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    MessageRepository messageRepository;
    AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(Message m){
        return messageRepository.save(m);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public void deleteMessage(Message m){
        messageRepository.delete(m);
    }

    public Message updatMessage(Message m, String message_text){
        List<Message> allMessages = messageRepository.findAll();
        for(Message message : allMessages){
            if(m.getMessage_id() == message.getMessage_id()){
                message.setMessage_text(message.getMessage_text());
                return message;
            }
        }        
        return null;
    }

    public List<Message> getMessagesForUser(int account_id){
        List<Message> messages = getAllMessages();
        List<Message> messagesForUser = new ArrayList<Message>();
        for(Message m : messages){
            if(m.getPosted_by() == account_id){
                messagesForUser.add(m);
            }
        }
        return messagesForUser;
    }

}
