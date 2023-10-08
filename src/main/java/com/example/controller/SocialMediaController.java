package com.example.controller;

import org.springframework.beans.factory.annotation.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

   //see JPA CRUD lab from 10/2 for more info on API structure

    @Autowired
    AccountService accountService;

    @Autowired
    MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account body) {

        Account newAccount = accountService.registerAccount(body);
    
        if(newAccount != null){
            return ResponseEntity.status(200).body(newAccount);
        }

        return ResponseEntity.status(409).body(null);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account body) {
        Account loginAccount = accountService.loginAccount(body);

        if(loginAccount != null){
            return ResponseEntity.status(200).body(loginAccount);
        }
        return ResponseEntity.status(401).body(null);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessage(@RequestBody Message body){
        List<Account> allAccounts = accountService.getAllAccounts();
        for(Account a : allAccounts){
            if(a.getAccount_id().equals(body.getPosted_by())){
                if(body.getMessage_text().length() > 0 && body.getMessage_text().length() < 255){
                    Message newMessage = messageService.sendMessage(body);
                    return ResponseEntity.status(200).body(newMessage);
                }
            }
        }
        
        return ResponseEntity.status(400).body(null);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages(){
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    //JPA's named query functionality does not work at all with the Account/Message variable names.
    //If I were to write Account and Message, I would rewrite the variable names to Java standard
    //(i.e. without underscores) so named queries could be used here. It would make this much more efficient.
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int message_id){
        List<Message> allMessages = messageService.getAllMessages();
        Message returnMessage = null;
        for(Message m : allMessages){
            if(m.getMessage_id() == message_id){
                returnMessage = m;
            }
        }
        return ResponseEntity.status(200).body(returnMessage);
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int message_id){
        List<Message> allMessages = messageService.getAllMessages();
        Message delete = null;
        for(Message m : allMessages){
            if(m.getMessage_id() == message_id){
                delete = m;
                messageService.deleteMessage(delete);
                return ResponseEntity.status(200).body(1);
            }
        }
        return ResponseEntity.status(200).body(0);
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int message_id,@RequestBody Message body){
        List<Message> allMessages = messageService.getAllMessages();
        for(Message m : allMessages){
            if(m.getMessage_id() == message_id){
                String s = body.getMessage_text();
                if(s.length() > 0 && s.length() < 255){
                    messageService.updatMessage(m,body.getMessage_text());
                    return ResponseEntity.status(200).body(1);
                }
            }
        }
        return ResponseEntity.status(400).body(0);
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesForUser(@PathVariable int account_id){
        return ResponseEntity.status(200).body(messageService.getMessagesForUser(account_id));
    }
}

