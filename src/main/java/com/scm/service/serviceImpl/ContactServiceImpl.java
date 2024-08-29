package com.scm.service.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.repositories.UserRepo;
import com.scm.service.ContactService;


@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepo contactRepo;

    
    @Override
    public Contact saveContact(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        contactRepo.save(contact);
        
        return contact;
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
    }

    @Override
    public Contact updateContact(Contact contact) {
        Contact contactFound = contactRepo.findById(contact.getId()).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        contactFound.setName(contact.getName());
        contactFound.setEmail(contact.getEmail());
        contactFound.setAddress(contact.getAddress());
        contactFound.setDescription(contact.getDescription());
        contactFound.setWebsiteLink(contact.getWebsiteLink());
        contactFound.setLinkedInLink(contact.getLinkedInLink());
        contactFound.setPicture(contact.getPicture());
        contactFound.setFavourite(contact.isFavourite());
        contactFound.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
        contactFound.setSocial(contact.getSocial());
        contactRepo.save(contactFound);
        return contactFound; 
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }

    @Override
    public void deleteContact(String id) {
        Contact contact = contactRepo.findById(id).orElse(null);
        if(contact != null){
            contactRepo.delete(contact);
        }
    }

    @Override
    public Page<Contact> getContactsByUser(User user, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pagebale = PageRequest.of(page - 1, size, sort);
        
        return contactRepo.findByUser(user, pagebale);
    }

    @Override
    public Page<Contact> searchByName(String name, int page, int size, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pagebale = PageRequest.of(page - 1, size, sort);

        return contactRepo.findByUserAndNameContaining(user,name, pagebale);
        
    }

    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pagebale = PageRequest.of(page - 1, size, sort);

        return contactRepo.findByUserAndEmailContaining(user,email, pagebale);
        
    }

    @Override
    public Page<Contact> searchByPhone(String phone, int page, int size, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pagebale = PageRequest.of(page - 1, size, sort);

        return contactRepo.findByUserAndPhoneNumberContaining(user, phone, pagebale);
        
    }



    
    
}
