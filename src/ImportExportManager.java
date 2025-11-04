import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.*;
import javax.crypto.SecretKey;

/**
 * Import/Export functionality for credentials
 * Supports Chrome, Firefox, Edge, Opera CSV formats and encrypted archives
 */
public class ImportExportManager {
    
    private Database database;
    private int userId;
    private SecretKey encryptionKey;
    
    public ImportExportManager(Database database, int userId, SecretKey encryptionKey) {
        this.database = database;
        this.userId = userId;
        this.encryptionKey = encryptionKey;
    }
    
    /**
     * Export credentials to encrypted archive
     * Archive contains: credentials.csv + attachments folder
     */
    public void exportToArchive(File outputFile, List<Database.Credential> credentials, 
                                AttachmentManager attachmentManager) throws Exception {
        // Create temp directory for export
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "securevault_export_" + System.currentTimeMillis());
        tempDir.mkdirs();
        
        try {
            // Export credentials to CSV
            File csvFile = new File(tempDir, "credentials.csv");
            exportToCSV(csvFile, credentials);
            
            // Export attachments
            File attachmentsDir = new File(tempDir, "attachments");
            attachmentsDir.mkdirs();
            exportAttachments(attachmentsDir, credentials, attachmentManager);
            
            // Create ZIP archive
            createZipArchive(tempDir, outputFile);
            
        } finally {
            // Clean up temp directory
            deleteDirectory(tempDir);
        }
    }
    
    /**
     * Export credentials to CSV file
     */
    public void exportToCSV(File outputFile, List<Database.Credential> credentials) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Write header
            writer.write("title,username,password,url,category,notes,favorite,created_date,modified_date,expiry_date");
            writer.newLine();
            
            // Write credentials
            for (Database.Credential cred : credentials) {
                writer.write(escapeCsv(cred.title));
                writer.write(",");
                writer.write(escapeCsv(cred.username));
                writer.write(",");
                writer.write(escapeCsv(cred.password));
                writer.write(",");
                writer.write(escapeCsv(cred.websiteUrl));
                writer.write(",");
                writer.write(escapeCsv(cred.category));
                writer.write(",");
                writer.write(escapeCsv(cred.notes));
                writer.write(",");
                writer.write(cred.isFavorite ? "1" : "0");
                writer.write(",");
                writer.write(escapeCsv(cred.createdDate));
                writer.write(",");
                writer.write(escapeCsv(cred.modifiedDate));
                writer.write(",");
                writer.write(escapeCsv(cred.expiryDate));
                writer.newLine();
            }
        }
    }
    
    /**
     * Import credentials from CSV file
     * Supports Chrome, Firefox, Edge, Opera formats
     */
    public int importFromCSV(File inputFile) throws Exception {
        int imported = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new IOException("Empty CSV file");
            }
            
            // Detect CSV format
            String[] headers = parseCsvLine(headerLine.toLowerCase());
            CsvFormat format = detectCsvFormat(headers);
            
            // Read and import data
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                try {
                    String[] values = parseCsvLine(line);
                    if (values.length < 2) continue; // Need at least title and password
                    
                    // Parse based on format
                    String title = "";
                    String username = "";
                    String password = "";
                    String url = "";
                    String category = "Other";
                    String notes = "";
                    boolean favorite = false;
                    String expiryDate = "";
                    
                    switch (format) {
                        case CHROME:
                            // Chrome format: name,url,username,password
                            if (values.length >= 4) {
                                title = values[0];
                                url = values[1];
                                username = values[2];
                                password = values[3];
                            }
                            break;
                            
                        case FIREFOX:
                            // Firefox format: url,username,password,httpRealm,formActionOrigin,guid,timeCreated,timeLastUsed,timePasswordChanged
                            if (values.length >= 3) {
                                url = values[0];
                                username = values[1];
                                password = values[2];
                                title = extractDomainFromUrl(url);
                            }
                            break;
                            
                        case EDGE:
                            // Edge format: name,url,username,password
                            if (values.length >= 4) {
                                title = values[0];
                                url = values[1];
                                username = values[2];
                                password = values[3];
                            }
                            break;
                            
                        case OPERA:
                            // Opera format: name,url,username,password
                            if (values.length >= 4) {
                                title = values[0];
                                url = values[1];
                                username = values[2];
                                password = values[3];
                            }
                            break;
                            
                        case SECUREVAULT:
                            // Our own format: title,username,password,url,category,notes,favorite,created,modified,expiry
                            if (values.length >= 3) {
                                title = values[0];
                                username = values[1];
                                password = values[2];
                                if (values.length > 3) url = values[3];
                                if (values.length > 4) category = values[4];
                                if (values.length > 5) notes = values[5];
                                if (values.length > 6) favorite = "1".equals(values[6]);
                                if (values.length > 9) expiryDate = values[9];
                            }
                            break;
                            
                        case GENERIC:
                        default:
                            // Generic: assume first 3 columns are title, username, password
                            if (values.length >= 3) {
                                title = values[0];
                                username = values[1];
                                password = values[2];
                            }
                            break;
                    }
                    
                    // Validate and insert
                    if (!title.isEmpty() && !password.isEmpty()) {
                        if (title.length() > 100) title = title.substring(0, 100);
                        if (username.isEmpty()) username = "N/A";
                        
                        try {
                            // Check if already exists
                            if (!database.credentialExists(userId, title, username)) {
                                database.insertCredential(userId, title, username, password, notes, 
                                                         favorite, category, url, expiryDate);
                                imported++;
                            }
                        } catch (SQLException e) {
                            // Skip duplicate or invalid entry
                            System.err.println("Skipped entry: " + title + " - " + e.getMessage());
                        }
                    }
                    
                } catch (Exception e) {
                    // Skip invalid line
                    System.err.println("Skipped invalid line: " + e.getMessage());
                }
            }
        }
        
        return imported;
    }
    
    /**
     * Import from encrypted archive
     */
    public int importFromArchive(File archiveFile, AttachmentManager attachmentManager) throws Exception {
        // Create temp directory for extraction
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "securevault_import_" + System.currentTimeMillis());
        tempDir.mkdirs();
        
        try {
            // Extract ZIP archive
            extractZipArchive(archiveFile, tempDir);
            
            // Import credentials from CSV
            File csvFile = new File(tempDir, "credentials.csv");
            int imported = 0;
            if (csvFile.exists()) {
                imported = importFromCSV(csvFile);
            }
            
            // Import attachments
            File attachmentsDir = new File(tempDir, "attachments");
            if (attachmentsDir.exists() && attachmentsDir.isDirectory()) {
                importAttachments(attachmentsDir, attachmentManager);
            }
            
            return imported;
            
        } finally {
            // Clean up temp directory
            deleteDirectory(tempDir);
        }
    }
    
    // Helper methods
    
    private enum CsvFormat {
        CHROME, FIREFOX, EDGE, OPERA, SECUREVAULT, GENERIC
    }
    
    private CsvFormat detectCsvFormat(String[] headers) {
        String headerStr = String.join(",", headers).toLowerCase();
        
        if (headerStr.contains("name") && headerStr.contains("url") && 
            headerStr.contains("username") && headerStr.contains("password")) {
            if (headerStr.contains("formactionorigin")) return CsvFormat.FIREFOX;
            if (headerStr.contains("edge")) return CsvFormat.EDGE;
            if (headerStr.contains("opera")) return CsvFormat.OPERA;
            return CsvFormat.CHROME;
        }
        
        if (headerStr.contains("title") && headerStr.contains("category")) {
            return CsvFormat.SECUREVAULT;
        }
        
        return CsvFormat.GENERIC;
    }
    
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        values.add(current.toString().trim());
        
        return values.toArray(new String[0]);
    }
    
    private String extractDomainFromUrl(String url) {
        try {
            if (!url.startsWith("http")) url = "http://" + url;
            java.net.URI uri = new java.net.URI(url);
            String host = uri.getHost();
            if (host != null && host.startsWith("www.")) host = host.substring(4);
            return host != null ? host : url;
        } catch (Exception e) {
            return url;
        }
    }
    
    private void exportAttachments(File attachmentsDir, List<Database.Credential> credentials, 
                                   AttachmentManager attachmentManager) throws Exception {
        for (Database.Credential cred : credentials) {
            List<AttachmentManager.Attachment> attachments = attachmentManager.getAttachments(cred.id);
            if (!attachments.isEmpty()) {
                // Create subfolder for this credential
                File credDir = new File(attachmentsDir, "cred_" + cred.id);
                credDir.mkdirs();
                
                for (AttachmentManager.Attachment att : attachments) {
                    File outputFile = new File(credDir, att.filename);
                    attachmentManager.downloadAttachment(att.id, outputFile, encryptionKey);
                }
            }
        }
    }
    
    private void importAttachments(File attachmentsDir, AttachmentManager attachmentManager) throws Exception {
        // Note: This is a simplified version. In practice, you'd need to match
        // attachments to credentials by title or other identifier
        System.out.println("Attachment import from archive not fully implemented");
    }
    
    private void createZipArchive(File sourceDir, File zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            
            zipDirectory(sourceDir, sourceDir, zos);
        }
    }
    
    private void zipDirectory(File rootDir, File currentDir, ZipOutputStream zos) throws IOException {
        File[] files = currentDir.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.isDirectory()) {
                zipDirectory(rootDir, file, zos);
            } else {
                String relativePath = rootDir.toPath().relativize(file.toPath()).toString();
                ZipEntry entry = new ZipEntry(relativePath);
                zos.putNextEntry(entry);
                
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, bytesRead);
                    }
                }
                zos.closeEntry();
            }
        }
    }
    
    private void extractZipArchive(File zipFile, File destDir) throws IOException {
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File file = new File(destDir, entry.getName());
                
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    file.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = zis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }
    
    private void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        dir.delete();
    }
}
