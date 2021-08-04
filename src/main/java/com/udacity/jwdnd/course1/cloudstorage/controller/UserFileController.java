package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import com.udacity.jwdnd.course1.cloudstorage.services.UserFileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class UserFileController {

    private UserFileService userFileService;
    private UserService userService;


    public UserFileController(UserFileService userFileService, UserService userService) {
        this.userFileService = userFileService;
        this.userService = userService;
    }

    @GetMapping("/files")
    public String getFilesPages(Model model, Authentication authentication) {
        String username = authentication.getName();

        User user = this.userService.getUser(username);

        String fullName = user.getFirstName() + " " + user.getLastName();

        int userid = this.getUserId(authentication);

        model.addAttribute("fileList", this.userFileService.getAllUserFiles(userid));
        model.addAttribute("userFullName", fullName);

        return "file-page";
    }

    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, Authentication authentication) throws IOException {

        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/files";
        }

        // check if fileName Exists
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (!userFileService.isFileNameAvailable(fileName)) {
            attributes.addFlashAttribute("message", fileName + " already exists");
            return "redirect:/files";
        }

        // save the file
        userFileService.upload(file, this.getUserId(authentication));

        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');
        return "redirect:/files";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") int id) throws Exception {
        try{
            UserFile userFile = this.userFileService.getFile(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(userFile.getContenttype()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userFile.getFilename() + "\"")
                    .body(new ByteArrayResource(userFile.getFiledata()));
        } catch (Exception e) {
            throw new Exception("Error downloading file");
        }

    }

    @GetMapping("/file/delete/{id}")
    public String deleteFile(@PathVariable("id") int id, RedirectAttributes attributes) {
        try {
            UserFile userFile = this.userFileService.getFile(id);
            this.userFileService.deleteFile(id);
            attributes.addFlashAttribute("message", userFile.getFilename() + " has been deleted Successfully");
            return "redirect:/files";

        }   catch (Exception e) {
            String errorMessage = "Could not Delete File";
            attributes.addFlashAttribute("message", errorMessage);
            return "redirect:/files";

        }

    }


    public int getUserId(Authentication authentication) {
        String username = authentication.getName();
        User user = this.userService.getUser(username);
        int userid = user.getUserid();
        return userid;
    }




}
