package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {

    private CredentialService credentialService;
    private UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping("/credentials")
    public String getCredentialPage(@ModelAttribute Credential credential, Model model, Authentication authentication) {

        String username = authentication.getName();

        User user = userService.getUser(username);
        String fullName = user.getFirstName() + " " + user.getLastName();
        int userId = user.getUserid();

        model.addAttribute("userFullName", fullName);
        model.addAttribute("userid", userId);
        model.addAttribute("credentials", credentialService.getAllUserCredentials(userId));

        return "credential-page";

    }


    @PostMapping("/create-credential")
    public String createCredential(@ModelAttribute Credential credential, Authentication authentication, RedirectAttributes attributes) {
        int userId = this.getUserId(authentication);
        try{
            credentialService.createCredential(credential, userId);
            attributes.addFlashAttribute("message", "Credential Added Successfully");
            return "redirect:/credentials";
        } catch (Exception e) {
            String errorMessage = "Could not add Credential";
            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/credentials";
        }
    }

    @GetMapping("/credential/view/{id}")
    @ResponseBody
    public Credential viewCredential(@PathVariable("id") int id, Model model) {
        Credential credential = credentialService.getCredential(id);
        return credential;
    }

    @PostMapping("/credential/update")
    public String updateCredential(Credential credential, Authentication authentication, RedirectAttributes attributes) {
        int userid = this.getUserId(authentication);
        try {
            credentialService.updateCredential(credential, userid);
            attributes.addFlashAttribute("message", "Note Updated Successfully");
            return "redirect:/credentials";
        } catch (Exception e) {
            String errorMessage = "Could not add Credential";
            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/credentials";
        }
    }

    @GetMapping("/credential/delete/{id}")
    public String deleteCredential(@PathVariable("id") int id, RedirectAttributes attributes) {
        try {
            credentialService.deleteNote(id);
            attributes.addFlashAttribute("message", "Note Deleted Successfully");
            return "redirect:/credentials";
        } catch (Exception e) {
            String errorMessage = "Could not delete Credential";
            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/credentials";
        }
    }


    public int getUserId(Authentication authentication) {
        String username = authentication.getName();
        User user = this.userService.getUser(username);
        int userid = user.getUserid();
        return userid;
    }





}
