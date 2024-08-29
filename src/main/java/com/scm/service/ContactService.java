package com.scm.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {
    Contact saveContact(Contact coontact);
    Contact getById(String id);
    Contact updateContact(Contact contact);
    List<Contact> getAllContacts();
    void deleteContact(String id);
    Page<Contact> getContactsByUser(User user, int page, int size, String sort, String direction);
    Page<Contact> searchByName(String name, int page, int size, String sortBy, String direction, User user);
    Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String direction, User user);
    Page<Contact> searchByPhone(String phoneNumber, int page, int size, String sortBy, String direction, User user);

}
