package com.scm.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.config.CronTask;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.form.ContactForm;
import com.scm.form.ContactSearchForm;
import com.scm.helpers.AppConstant;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.repositories.ContactRepo;
import com.scm.service.ContactService;
import com.scm.service.ImageService;
import com.scm.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.val;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {
    
    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    
    @RequestMapping("/add")
    public String add_contacts(Model model){
        ContactForm contact = new ContactForm();
        model.addAttribute("contactForm", contact);
        System.out.println("contactinfo " + contact.getContactEmail());
        return "user/addContact";
    }

    @PostMapping("adding")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult, Authentication authentication,  HttpSession session){
        if(bindingResult.hasErrors()){
                Message message = Message.builder().content("Please correct the following errors!").type(MessageType.red).build();
                session.setAttribute("message", message);
            return "/user/addContact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        
        Contact contact = new Contact();
        String filename = UUID.randomUUID().toString();
        String fileUrl = imageService.uploadImge(contactForm.getImage(), filename);
        contact.setName(contactForm.getContactName());
        contact.setEmail(contactForm.getContactEmail());
        contact.setPhoneNumber(contactForm.getNumber());
        contact.setAddress(contactForm.getContactAddress());
        contact.setDescription(contactForm.getContactDescription());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPicture(fileUrl);
        contact.setUser(user);
        contact.setFavourite(contactForm.isFavourite());
        contact.setCloudinaryImagePublicId(filename);
        contactService.saveContact(contact);
        Message message = Message.builder().content("Contact Added Successfully").type(MessageType.green).build();
        session.setAttribute("message", message);
        return "redirect:/user/contacts/add";
    }


    @GetMapping("/view")
    public String getAllContacts(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE) int size, @RequestParam(value = "sortBy", defaultValue = "name") String sortBy, @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model, Authentication authentication){
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContact = contactService.getContactsByUser(user, page, size, sortBy, direction);
        System.out.println("pageNumber " + pageContact.getNumber() + " pageSize " + pageContact.getContent());
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstant.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        
        return "/user/contacts";
    }

    @GetMapping("/search")
    public String deleteContact(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE) int size, @RequestParam(value = "sortBy", defaultValue = "name") String sortBy, @RequestParam(value = "direction", defaultValue = "asc") String direction, @ModelAttribute ContactSearchForm contactSearchForm, Model model, Authentication authentication){
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> contact = null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            System.out.println("entered");
            contact = contactService.searchByName(contactSearchForm.getValue(),page,size,sortBy, direction, user);
            System.out.println("getNumberOfElements" + contact.getNumberOfElements());
        }
        if(contactSearchForm.getField().equalsIgnoreCase("email")){
            contact = contactService.searchByEmail(contactSearchForm.getValue(),page,size,sortBy, direction, user);
            System.out.println("getNumberOfElements" + contact.getNumberOfElements());
        }
        if(contactSearchForm.getField().equalsIgnoreCase("phone")){
            contact = contactService.searchByPhone(contactSearchForm.getValue(),page,size,sortBy, direction, user);
            System.out.println("getNumberOfElements" + contact.getNumberOfElements());
        }
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("searchedContact", contact);
        model.addAttribute("pageSize", AppConstant.PAGE_SIZE);
        return "/user/search";
    }

    @RequestMapping("/delete/{id}")
    public String deletionContact(@PathVariable String id){
        contactService.deleteContact(id);
        return "redirect:/user/contacts/view";
    }

    @RequestMapping("/edit/{id}")
    public String editContact(@PathVariable String id, Model model){
        var contact = contactService.getById(id);

        ContactForm contactForm = new ContactForm();
        contactForm.setContactName(contact.getName());
        contactForm.setContactEmail(contact.getEmail());
        contactForm.setNumber(contact.getPhoneNumber());
        contactForm.setContactAddress(contact.getAddress());
        contactForm.setContactDescription(contact.getDescription());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setPicture(contact.getPicture());
        contactForm.setFavourite(contact.isFavourite());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", id);



        return "user/edit";
    }

    @PostMapping("/update/{id}")
    public String updateContact(@PathVariable String id, @Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "user/edit";
        }
        
        Contact contactFound = contactService.getById(id);
        contactFound.setId(id);
        contactFound.setPicture(contactFound.getPicture());
        contactFound.setName(contactForm.getContactName());
        contactFound.setEmail(contactForm.getContactEmail());
        contactFound.setAddress(contactForm.getContactAddress());
        contactFound.setPhoneNumber(contactForm.getNumber());
        contactFound.setDescription(contactForm.getContactDescription());
        contactFound.setWebsiteLink(contactForm.getWebsiteLink());
        contactFound.setLinkedInLink(contactForm.getLinkedInLink());
        contactFound.setFavourite(contactForm.isFavourite());
        if(contactForm.getImage() != null && !contactForm.getImage().isEmpty()){
            String filename = UUID.randomUUID().toString();
            String imageURL = imageService.uploadImge(contactForm.getImage(), filename);
            contactFound.setCloudinaryImagePublicId(filename);
            contactFound.setPicture(imageURL);
            contactForm.setPicture(imageURL);
        }
        contactService.updateContact(contactFound);
        return "redirect:/user/contacts/edit/" + id;
        
    }


}


