# Duplicate Credential Prevention - Implementation Summary

## **Change Implemented: Prevent Duplicate Title + Username Combinations**

---

## **Problem:**
Previously, users could create multiple credentials with the same title and username combination. For example:
- **First Entry:** `Instagram`, `abin_bethanya`, `testingpass`
- **Second Entry:** `Instagram`, `abin_bethanya`, `againtesting` ← This should NOT be allowed

This creates confusion as there's no way to distinguish which password is the correct/current one.

---

## **Solution:**
Added validation to ensure each user can only have **ONE unique credential** per Title + Username combination.

---

## **Technical Changes:**

### **1. New Methods in `Database.java`:**

#### **`credentialExists(userId, title, username)`**
```java
public boolean credentialExists(int userId, String title, String username) throws SQLException
```
- Checks if a credential with the same title AND username already exists for this user
- Uses **case-insensitive comparison** (LOWER() function in SQL)
- Returns `true` if duplicate exists, `false` otherwise

#### **`credentialExistsExcludingId(userId, title, username, excludeId)`**
```java
public boolean credentialExistsExcludingId(int userId, String title, String username, int excludeId) throws SQLException
```
- Same as above, but **excludes a specific credential ID**
- Used during **EDIT** operations to allow updating the same credential without triggering duplicate error
- Example: User can change password for existing Instagram/abin_bethanya without error

### **2. Modified `insertCredential()` Method:**
**Before:**
```java
public void insertCredential(int userId, String title, String username, String password) throws SQLException {
    String sql = "INSERT INTO credentials (user_id, title, username, password) VALUES (?, ?, ?, ?)";
    // Direct insert without checking
}
```

**After:**
```java
public void insertCredential(int userId, String title, String username, String password) throws SQLException {
    // Check for duplicates BEFORE inserting
    if (credentialExists(userId, title, username)) {
        throw new SQLException("A credential with this title and username already exists!");
    }
    
    String sql = "INSERT INTO credentials (user_id, title, username, password) VALUES (?, ?, ?, ?)";
    // Rest of insert code...
}
```

### **3. Modified `updateCredential()` Method:**
**Before:**
```java
public void updateCredential(int id, String title, String username, String password) throws SQLException {
    String sql = "UPDATE credentials SET title = ?, username = ?, password = ? WHERE id = ?";
    // Direct update without checking
}
```

**After:**
```java
public void updateCredential(int id, String title, String username, String password) throws SQLException {
    // Get the user_id for this credential
    int userId = getUserIdForCredential(id);
    
    // Check for duplicates EXCLUDING the current credential being edited
    if (userId != -1 && credentialExistsExcludingId(userId, title, username, id)) {
        throw new SQLException("A credential with this title and username already exists!");
    }
    
    String sql = "UPDATE credentials SET title = ?, username = ?, password = ? WHERE id = ?";
    // Rest of update code...
}
```

---

## **How It Works:**

### **Scenario 1: Adding New Credential (Success)**
```
User: alice (user_id = 1)
Action: Add credential
Input: Title="Instagram", Username="abin_bethanya", Password="MyPass123"

Flow:
1. User clicks "Add" button
2. CredentialDialog opens and user enters data
3. database.insertCredential(1, "Instagram", "abin_bethanya", "MyPass123")
4. credentialExists() checks database:
   - Query: SELECT COUNT(*) WHERE user_id=1 AND title='Instagram' AND username='abin_bethanya'
   - Result: 0 (no existing credential)
5. No duplicate found → Insert proceeds successfully ✅
6. Credential saved to database
```

### **Scenario 2: Adding Duplicate Credential (Blocked)**
```
User: alice (user_id = 1)
Existing: Instagram/abin_bethanya already exists
Action: Add credential again
Input: Title="Instagram", Username="abin_bethanya", Password="DifferentPass456"

Flow:
1. User clicks "Add" button
2. CredentialDialog opens and user enters data
3. database.insertCredential(1, "Instagram", "abin_bethanya", "DifferentPass456")
4. credentialExists() checks database:
   - Query: SELECT COUNT(*) WHERE user_id=1 AND title='Instagram' AND username='abin_bethanya'
   - Result: 1 (credential exists!)
5. Duplicate found → throw SQLException("A credential with this title and username already exists!") ❌
6. SecureVaultSwing catches exception and shows error dialog
7. User sees: "Failed to add credential: A credential with this title and username already exists!"
```

### **Scenario 3: Editing Existing Credential (Success)**
```
User: alice (user_id = 1)
Existing: Instagram/abin_bethanya/OldPassword (id = 5)
Action: Edit credential to change password
Input: Title="Instagram", Username="abin_bethanya", Password="NewPassword789"

Flow:
1. User selects row and clicks "Edit"
2. CredentialDialog opens with existing data
3. User changes password field only
4. database.updateCredential(5, "Instagram", "abin_bethanya", "NewPassword789")
5. credentialExistsExcludingId() checks database:
   - Query: SELECT COUNT(*) WHERE user_id=1 AND title='Instagram' AND username='abin_bethanya' AND id != 5
   - Result: 0 (no OTHER credential with same title/username)
6. No duplicate found → Update proceeds successfully ✅
7. Password updated in database
```

### **Scenario 4: Editing to Create Duplicate (Blocked)**
```
User: alice (user_id = 1)
Existing Credentials:
  - Instagram/abin_bethanya (id = 5)
  - Facebook/abin_bethanya (id = 6)
Action: Edit Facebook credential to change title to "Instagram"
Input: Title="Instagram", Username="abin_bethanya", Password="FacebookPass"

Flow:
1. User selects Facebook row and clicks "Edit"
2. CredentialDialog opens with existing data
3. User changes title from "Facebook" to "Instagram"
4. database.updateCredential(6, "Instagram", "abin_bethanya", "FacebookPass")
5. credentialExistsExcludingId() checks database:
   - Query: SELECT COUNT(*) WHERE user_id=1 AND title='Instagram' AND username='abin_bethanya' AND id != 6
   - Result: 1 (credential with id=5 has same title/username!)
6. Duplicate found → throw SQLException("A credential with this title and username already exists!") ❌
7. User sees error: "Failed to update credential: A credential with this title and username already exists!"
```

---

## **Case Sensitivity:**

The comparison is **case-insensitive** to prevent sneaky duplicates:
- `Instagram` = `instagram` = `INSTAGRAM` = `InStAgRaM`
- `abin_bethanya` = `Abin_Bethanya` = `ABIN_BETHANYA`

This is achieved using SQL's `LOWER()` function:
```sql
WHERE LOWER(title) = LOWER(?) AND LOWER(username) = LOWER(?)
```

---

## **Allowed Scenarios:**

These combinations are **ALLOWED** and do NOT trigger duplicate errors:

### ✅ **Different Username (Same Title):**
```
Instagram, abin_bethanya, password1    ← Allowed
Instagram, john_doe, password2         ← Allowed (different username)
```

### ✅ **Different Title (Same Username):**
```
Instagram, abin_bethanya, password1    ← Allowed
Facebook, abin_bethanya, password2     ← Allowed (different title)
```

### ✅ **Different User (Same Title + Username):**
```
User: alice
  Instagram, abin_bethanya, password1  ← Allowed

User: bob
  Instagram, abin_bethanya, password2  ← Allowed (different user)
```

---

## **Blocked Scenarios:**

These combinations are **BLOCKED** and trigger duplicate errors:

### ❌ **Exact Duplicate (Same Title + Username):**
```
User: alice
  Instagram, abin_bethanya, password1  ← Existing
  Instagram, abin_bethanya, password2  ← BLOCKED!
```

### ❌ **Case-Insensitive Duplicate:**
```
User: alice
  Instagram, abin_bethanya, password1  ← Existing
  INSTAGRAM, ABIN_BETHANYA, password2  ← BLOCKED! (treated as duplicate)
```

---

## **Error Messages:**

### **When Adding Duplicate:**
```
Failed to add credential: A credential with this title and username already exists!
```

### **When Editing to Create Duplicate:**
```
Failed to update credential: A credential with this title and username already exists!
```

---

## **Benefits:**

1. **Data Integrity:** Prevents confusion from multiple passwords for the same account
2. **User Experience:** Clear error messages guide users to fix the issue
3. **Database Consistency:** Ensures one-to-one mapping between (title, username) and password
4. **Security:** Users always know which password is current for each account
5. **Prevents Mistakes:** Stops accidental duplicate entries

---

## **Testing:**

### **Test Case 1: Add Duplicate**
```
1. Add: Instagram, abin_bethanya, pass123
   Result: ✅ Success
2. Add: Instagram, abin_bethanya, pass456
   Result: ❌ Error shown
```

### **Test Case 2: Edit Existing**
```
1. Existing: Instagram, abin_bethanya, oldpass
2. Edit password to: newpass123
   Result: ✅ Success (same title/username, just password change)
```

### **Test Case 3: Edit to Duplicate**
```
1. Existing: Instagram, abin_bethanya, pass1
2. Existing: Facebook, abin_bethanya, pass2
3. Edit Facebook title to "Instagram"
   Result: ❌ Error shown (would create duplicate)
```

### **Test Case 4: Different Usernames**
```
1. Add: Instagram, abin_bethanya, pass123
   Result: ✅ Success
2. Add: Instagram, john_doe, pass456
   Result: ✅ Success (different username)
```

---

## **Implementation Complete! 🎉**

The duplicate prevention is now active. Users will immediately see error messages if they try to create duplicate credentials, making the application more robust and user-friendly.
