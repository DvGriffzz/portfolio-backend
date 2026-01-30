package com.diogo.portfolio_backend.service;

import com.diogo.portfolio_backend.dto.ContactRequest;
import com.diogo.portfolio_backend.entity.ContactMessage;
import com.diogo.portfolio_backend.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    private final ContactMessageRepository repository;
    public ContactService(ContactMessageRepository repository){
        this.repository = repository;
    }
    public void saveMessage(ContactRequest request){
        ContactMessage msg = new ContactMessage();
        msg.setName(request.getName());
        msg.setEmail(request.getEmail());
        msg.setMessage(request.getMessage());
        repository.save(msg);
    }
    public List<ContactMessage> getAllMessages(){
        return repository.findAll();
    }
    public void deleteMessage(Long id){
        repository.deleteById(id);
    }
}
