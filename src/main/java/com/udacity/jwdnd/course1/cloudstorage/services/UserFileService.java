package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserFileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserFileService {

    private final UserFileMapper userFileMapper;

    public UserFileService(UserFileMapper userFileMapper) {
        this.userFileMapper = userFileMapper;
    }

    /*
    * Get all the Files Uploaded by the User
    *
    * @param userId the id of the user
    * @return List<UserFile>
    * */
    public List<UserFile> getAllUserFiles(int userId) {
        return userFileMapper.getUserFiles(userId);
    }

    /*
    * Upload a File
    *
    * @param MultipartFile file, int userid
    * @return int row of the Table
    * */
    public int upload(MultipartFile file, int userid) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        UserFile userFile = new UserFile(null, filename, file.getContentType(),
                file.getSize(), userid, file.getBytes());

        return userFileMapper.insert(userFile);
    }

    /*
    * Get a File
    *
    * @param int id
    * @return UserFile
    * */
    public UserFile getFile(int id) {
        return userFileMapper.findById(id);
    }

    /*
    * Delete a File
    *
    * @param int id
    * @return void
    * */
    public void deleteFile(int id) {
        userFileMapper.delete(id);
    }

    public boolean isFileNameAvailable(String filename) {
        return userFileMapper.getFile(filename) == null;
    }




}
