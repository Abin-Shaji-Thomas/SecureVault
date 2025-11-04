import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

/**
 * Handles encryption and decryption of passwords using AES-256 in CBC mode.
 * Uses PBKDF2 to derive encryption keys from user's master password.
 * 
 * Security Features:
 * - AES-256 encryption
 * - Random IV for each encryption
 * - PBKDF2 key derivation (100,000 iterations)
 * - Salt-based key generation
 */
public class PasswordEncryption {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_LENGTH = 256;
    private static final int ITERATION_COUNT = 100000;
    private static final int IV_LENGTH = 16;
    
    /**
     * Derives an AES encryption key from a master password using PBKDF2.
     * 
     * @param masterPassword The user's master password
     * @param salt Salt for key derivation (should be user's password salt)
     * @return SecretKey for AES encryption/decryption
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static SecretKey deriveKey(String masterPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(
            masterPassword.toCharArray(), 
            salt, 
            ITERATION_COUNT,
            KEY_LENGTH
        );
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), KEY_ALGORITHM);
    }
    
    /**
     * Encrypts plaintext using AES-256-CBC.
     * Generates a random IV and prepends it to the ciphertext.
     * 
     * @param plaintext The text to encrypt
     * @param key The encryption key
     * @return Base64-encoded string containing IV + ciphertext
     * @throws Exception If encryption fails
     */
    public static String encrypt(String plaintext, SecretKey key) throws Exception {
        if (plaintext == null || plaintext.isEmpty()) {
            return "";
        }
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        
        // Generate random IV (Initialization Vector)
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        
        // Encrypt
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        
        // Combine IV + encrypted data
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        
        // Return as Base64 string
        return Base64.getEncoder().encodeToString(combined);
    }
    
    /**
     * Decrypts ciphertext using AES-256-CBC.
     * Extracts IV from the beginning of the ciphertext.
     * 
     * @param ciphertext Base64-encoded string containing IV + ciphertext
     * @param key The decryption key
     * @return Decrypted plaintext
     * @throws Exception If decryption fails
     */
    public static String decrypt(String ciphertext, SecretKey key) throws Exception {
        if (ciphertext == null || ciphertext.isEmpty()) {
            return "";
        }
        
        // Decode from Base64
        byte[] combined = Base64.getDecoder().decode(ciphertext);
        
        // Extract IV and encrypted data
        byte[] iv = new byte[IV_LENGTH];
        byte[] encrypted = new byte[combined.length - IV_LENGTH];
        System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
        System.arraycopy(combined, IV_LENGTH, encrypted, 0, encrypted.length);
        
        // Decrypt
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decrypted = cipher.doFinal(encrypted);
        
        return new String(decrypted, StandardCharsets.UTF_8);
    }
    
    /**
     * Generates a cryptographically secure random salt.
     * 
     * @param length Length of salt in bytes
     * @return Random salt bytes
     */
    public static byte[] generateSalt(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
    
    /**
     * Securely clears a SecretKey from memory (best effort).
     * Note: Java doesn't guarantee memory is actually cleared.
     * 
     * @param key The key to clear
     */
    public static void clearKey(SecretKey key) {
        if (key != null) {
            try {
                // Overwrite key bytes with zeros
                byte[] encoded = key.getEncoded();
                if (encoded != null) {
                    java.util.Arrays.fill(encoded, (byte) 0);
                }
            } catch (Exception e) {
                // Best effort - may not be supported by all key types
            }
        }
    }
}
