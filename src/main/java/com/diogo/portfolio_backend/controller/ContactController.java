package com.diogo.portfolio_backend.controller;

import com.diogo.portfolio_backend.dto.ContactRequest;
import com.diogo.portfolio_backend.entity.ContactMessage;
import com.diogo.portfolio_backend.service.ContactService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin //permite chamar do Angular a API
public class ContactController {
    private final ContactService service;
    public ContactController(ContactService service){
        this.service = service;
    }

    // Public endpoint to submit contact form
    @PostMapping("/public/contact")
    public void sendMessage(@RequestBody ContactRequest request){
        service.saveMessage(request);
    }

    // Admin endpoint to view the messages
    @GetMapping("/admin/messages")
    public List<ContactMessage> getMessages(){
        return service.getAllMessages();
    }

    // Admin endpoint to delete a message
    @DeleteMapping("/admin/messages/{id}")
    public void deleteMessage(@PathVariable Long id){
        service.deleteMessage(id);
    }
}
