package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/notes")
    public String getNotePage(@ModelAttribute Note note, Model model, Authentication authentication) {
        String username = authentication.getName();

        User user = userService.getUser(username);
        String fullName = user.getFirstName() + " " + user.getLastName();
        int userId = user.getUserid();

        note.setUserid(userId);

        model.addAttribute("userFullName", fullName);
        model.addAttribute("userid", userId);
        model.addAttribute("notes", noteService.getUserNotes(userId));


        return "note-page";
    }

    @PostMapping("/create-note")
    public String createNote(@ModelAttribute Note note, Authentication authentication, RedirectAttributes attributes) {
        int userId = this.getUserId(authentication);
        note.setUserid(userId);
        try{
            noteService.createNote(note, userId);
            attributes.addFlashAttribute("message", "Note Added Successfully");
            return "redirect:/notes";
        } catch (Exception e) {
            String errorMessage = "Could not add Note";
            attributes.addFlashAttribute("message", errorMessage);
            return "redirect:/notes";
        }

    }

    @GetMapping("/notes/view/{id}")
    public void viewNote(@PathVariable("id") int id, Model model, RedirectAttributes attributes) {
        Note note = this.noteService.getNote(id);
        model.addAttribute("note", note);
    }

    @PostMapping("/note/update")
    public String updateNote(Note note, Authentication authentication, RedirectAttributes attributes) {
        int userid = this.getUserId(authentication);
        try {
            noteService.updateNote(note, userid);
            attributes.addFlashAttribute("message", "Note Updated Successfully");
            return "redirect:/notes";
        } catch (Exception e) {
            String errorMessage = "Could not update Note";
            attributes.addFlashAttribute("message", errorMessage);
            return "redirect:/notes";
        }
    }

    @GetMapping("/notes/delete/{id}")
    public String deleteNote(@PathVariable("id") int id, RedirectAttributes attributes) {
        try {
            noteService.deleteNote(id);
            attributes.addFlashAttribute("message", "Note deleted Successfully");
            return "redirect:/notes";
        } catch (Exception e) {
            String errorMessage = "Could not Delete File";
            attributes.addFlashAttribute("message", errorMessage);
            return "redirect:/notes";
        }
    }




    public int getUserId(Authentication authentication) {
        String username = authentication.getName();
        User user = this.userService.getUser(username);
        int userid = user.getUserid();
        return userid;
    }
}
