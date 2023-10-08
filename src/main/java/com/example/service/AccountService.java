package com.example.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    
    public Account registerAccount(Account account){
        if(account.getUsername().length() > 0 && account.getPassword().length() > 3){
            if(accountRepository.findAccountByUsername(account.getUsername()) == null){
                return accountRepository.save(account);
            }
        }
        return null;
    }


    public Account loginAccount(Account account){
        Account login = accountRepository.findAccountByUsername(account.getUsername());
        if (login != null && login.getPassword().equals(account.getPassword())){
            return login;
        }
        return null;
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

}
