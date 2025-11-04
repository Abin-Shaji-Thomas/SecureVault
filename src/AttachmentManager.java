import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;

/**
 * Manages encrypted file attachments for credentials
 */
public class AttachmentManager {
    
    private Connection connection;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    
    public AttachmentManager(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Attachment data class
     */
    public static class Attachment {
        public final int id;
        public final int credentialId;
        public final String filename;
        public final long fileSize;
        public final String uploadDate;
        
        public Attachment(int id, int credentialId, String filename, long fileSize, String uploadDate) {
            this.id = id;
            this.credentialId = credentialId;
            this.filename = filename;
            this.fileSize = fileSize;
            this.uploadDate = uploadDate;
        }
    }
    
    /**
     * Add an attachment to a credential
     */
    public void addAttachment(int credentialId, File file, SecretKey encryptionKey) throws Exception {
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File does not exist or is not a file");
        }
        
        if (file.length() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds maximum limit of " + (MAX_FILE_SIZE / 1024 / 1024) + " MB");
        }
        
        // Read file data
        byte[] fileData = readFileBytes(file);
        
        // Encrypt file data
        byte[] encryptedData = fileData;
        if (encryptionKey != null) {
            String base64Data = java.util.Base64.getEncoder().encodeToString(fileData);
            String encryptedBase64 = PasswordEncryption.encrypt(base64Data, encryptionKey);
            encryptedData = encryptedBase64.getBytes("UTF-8");
        }
        
        // Store in database
        String sql = "INSERT INTO attachments (credential_id, filename, file_data, file_size, encrypted, upload_date) VALUES (?, ?, ?, ?, ?, datetime('now'))";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, credentialId);
            pstmt.setString(2, file.getName());
            pstmt.setBytes(3, encryptedData);
            pstmt.setLong(4, file.length());
            pstmt.setBoolean(5, encryptionKey != null);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Get all attachments for a credential
     */
    public List<Attachment> getAttachments(int credentialId) throws SQLException {
        List<Attachment> attachments = new ArrayList<>();
        String sql = "SELECT id, credential_id, filename, file_size, upload_date FROM attachments WHERE credential_id = ? ORDER BY upload_date DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, credentialId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                attachments.add(new Attachment(
                    rs.getInt("id"),
                    rs.getInt("credential_id"),
                    rs.getString("filename"),
                    rs.getLong("file_size"),
                    rs.getString("upload_date")
                ));
            }
        }
        
        return attachments;
    }
    
    /**
     * Download an attachment
     */
    public void downloadAttachment(int attachmentId, File outputFile, SecretKey encryptionKey) throws Exception {
        String sql = "SELECT file_data, encrypted FROM attachments WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, attachmentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                byte[] encryptedData = rs.getBytes("file_data");
                boolean isEncrypted = rs.getBoolean("encrypted");
                
                byte[] fileData = encryptedData;
                
                // Decrypt if necessary
                if (isEncrypted && encryptionKey != null) {
                    String encryptedBase64 = new String(encryptedData, "UTF-8");
                    String decryptedBase64 = PasswordEncryption.decrypt(encryptedBase64, encryptionKey);
                    fileData = java.util.Base64.getDecoder().decode(decryptedBase64);
                }
                
                // Write to file
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    fos.write(fileData);
                }
            } else {
                throw new SQLException("Attachment not found");
            }
        }
    }
    
    /**
     * Delete an attachment
     */
    public void deleteAttachment(int attachmentId) throws SQLException {
        String sql = "DELETE FROM attachments WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, attachmentId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Delete all attachments for a credential
     */
    public void deleteAllAttachments(int credentialId) throws SQLException {
        String sql = "DELETE FROM attachments WHERE credential_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, credentialId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Get attachment count for a credential
     */
    public int getAttachmentCount(int credentialId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM attachments WHERE credential_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, credentialId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    /**
     * Get total size of attachments for a credential
     */
    public long getTotalAttachmentSize(int credentialId) throws SQLException {
        String sql = "SELECT SUM(file_size) FROM attachments WHERE credential_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, credentialId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }
    
    /**
     * Read file into byte array
     */
    private byte[] readFileBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }
    
    /**
     * Format file size for display
     */
    public static String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        else if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        else return String.format("%.2f MB", size / 1024.0 / 1024.0);
    }
}
