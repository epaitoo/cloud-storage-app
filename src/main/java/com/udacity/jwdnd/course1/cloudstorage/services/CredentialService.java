package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    /*
     * Get all the Credentials created by the User
     *
     * @param userId the id of the user
     * @return List<Credential>
     * */
    public List<Credential> getAllUserCredentials(int userid) {
        return credentialMapper.getCredentials(userid);
    }

    /*
     * Create A Credential
     *
     * @param Credential credential, int userid
     * @return int row of the Table
     * */
    public int createCredential(Credential credential, int userid) {
        String password = credential.getPassword();
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        return credentialMapper.insert(new Credential(null, credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, userid));
    }

    /*
     * View A Credential
     *
     * @param int id
     * @return Credential credential
     * */
    public Credential getCredential(int id) {
        Credential credential = credentialMapper.findById(id);
        String encryptedPassword =  credential.getPassword();
        String key = credential.getKey();
        String decryptPassword = this.decryptPassword(encryptedPassword, key);
        credential.setPassword(decryptPassword);
        return credential;
    }

    /*
     * Update A Credential
     *
     * @param Credential credential
     * @return void
     * */
    public void updateCredential(Credential credential, int userid) {
        String password = credential.getPassword();
        String encryptedPassword = encryptionService.encryptValue(password, credential.getKey());
        credentialMapper.update(new Credential(credential.getCredentialid(), credential.getUrl(), credential.getUsername(), credential.getKey(), encryptedPassword, userid));
    }

    /*
     * Delete A Credential
     *
     * @param int id
     * @return void
     * */
    public void deleteNote(int id) {
        credentialMapper.delete(id);
    }

    /*
     * Decrypt A Credential Password
     *
     * @param String encryptedPassword, String encodedkey
     * @return String decryptedPassword
     * */
    public String decryptPassword(String encryptedPassword, String encodedKey) {
        return encryptionService.decryptValue(encryptedPassword, encodedKey);
    }


}
