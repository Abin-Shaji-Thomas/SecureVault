# Changelog - SecureVault Pro

All notable changes to this project will be documented in this file.

---

## [3.0.0] - October 29, 2025

### 🎉 Major Release - Advanced Password Management

#### Added
- **Categories System** 
  - 7 default categories (Social Media, Banking, Email, Work, Shopping, Entertainment, Other)
  - Custom category creation and management
  - Color-coded category badges in table view
  - Category dropdown in Add/Edit dialog with live category loading
  
- **Website URL Integration**
  - Website URL field for each credential
  - Quick "Open in Browser" button (🌐) to launch URLs directly
  - Automatic URL validation and sanitization
  
- **Password Expiry Tracking**
  - Set expiry dates for credentials
  - Visual expiry warnings (Red: Expired, Amber: Expiring <30 days, Green: Valid)
  - Quick "+90 Days" button for convenient date setting
  - Expiry column in table view with color indicators
  - Automatic expiry notifications in Health Dashboard
  
- **Import/Export Manager**
  - Import from CSV files with automatic field mapping
  - Import from encrypted ZIP archives
  - Export to password-protected ZIP with credentials.csv and attachments
  - Duplicate detection during import (title + username)
  - Organized export structure
  
- **Encrypted Attachments**
  - Upload files up to 10 MB per credential
  - Encrypted storage in database as BLOBs
  - Download and view attachments
  - Delete individual attachments
  - Attachment count tracking
  - Perfect for 2FA backup codes, recovery codes, IDs, documents
  
- **Health Dashboard**
  - Security score calculation (0-100) based on password strength distribution
  - Visual statistics: Total, Weak, Medium, Strong password counts
  - Expired and expiring soon alerts
  - Action items list with direct edit links
  - Beautiful gradient UI with custom progress bars
  - Penalties for reused, expired, and expiring passwords

#### Enhanced
- **Table View** - Expanded to 8 columns (⭐ Favorite | Title | Username | Password | Category | Strength | Expiry | Modified)
- **Dialog** - Added 4 new fields (Category, Website, Expiry, Attachments) with smart buttons
- **Toolbar** - Added Import, Export, and Health Dashboard buttons (now 13 total)
- **Database Schema** - Added columns: category, website_url, expiry_date, last_password_change
- **Database Schema** - Added tables: custom_categories, attachments

#### Fixed
- **Health Dashboard Score Bug** - Fixed incorrect 0/100 score calculation
  - Was treating 0-6 strength score as 0-100 range
  - Now correctly uses StrengthChecker.Strength enum (WEAK/MEDIUM/STRONG)
  - Implemented proper security score algorithm with penalties
  
- **Health Dashboard UI** - Fixed white empty panels
  - Replaced broken progress bars with custom-drawn progress bar
  - Added gradient header with shine effect
  - Fixed layout issues with BoxLayout
  - Added proper sizing and resizing support
  
- **Search Field Theme** - Fixed black text in light mode
  - Added proper caret color setting
  - Fixed background and border colors
  - Improved theme consistency
  
- **+90 Days Button** - Verified working correctly
  - Sets expiry to current date + 90 days
  - Correctly formats date as YYYY-MM-DD
  - Updates field immediately on click
  
- **Deprecation Warning** - Fixed java.net.URL deprecation
  - Replaced deprecated URL constructor with URI.toURL()
  - Updated ImportExportManager.java
  
- **Compiler Warnings** - Fixed all warnings
  - Eliminated "this-escape" warnings in constructors
  - Added serialVersionUID to serializable classes
  - Made overridable methods final where appropriate
  - Suppressed false-positive unused field warnings
  - **Result:** Clean compilation with `-Xlint:all`

#### Optimized
- **Code Quality** - Comprehensive cleanup and optimization
  - Removed unused imports
  - Eliminated dead code
  - Consistent code formatting
  - Added debug logging for troubleshooting
  - Removed temporary migration utilities
  
- **Database Operations** - Improved efficiency
  - Optimized credential loading with proper column mapping
  - Added database upgrade system for schema changes
  - Automatic table creation for new features
  
- **UI Performance** - Improved rendering
  - Custom table renderers for categories and expiry
  - Efficient color coding and visual indicators
  - Smooth theme transitions

#### Documentation
- **README.md** - Complete rewrite
  - Clean, professional structure
  - Comprehensive feature documentation
  - Usage guide with examples
  - Technical details and architecture
  - Development instructions
  
- **FIXES_APPLIED.md** - Updated with all recent fixes
- **FEATURES.md** - Detailed feature documentation (existing)
- **CHANGELOG.md** - This file

---

## [2.0.0] - Previous Release

### Added
- Dark/Light theme support with Samsung-inspired gradients
- Favorites system with golden star markers
- Password strength analysis with visual indicators
- Real-time search functionality (Ctrl+F)
- Smart filters (All, Favorites, Weak/Medium/Strong)
- 6 sort options (Title, Username, Dates, Favorites)
- Keyboard shortcuts for common actions
- Session timeout (5 minutes)
- Secure clipboard with auto-clear (30 seconds)

### Enhanced
- Modern UI design
- Improved table view with alternating colors
- Enhanced credential dialog
- Better color schemes

---

## [1.0.0] - Initial Release

### Added
- Multi-user support with separate vaults
- AES-256-CBC encryption for passwords
- PBKDF2 key derivation (100,000 iterations)
- SHA-256 authentication with salted passwords
- Basic CRUD operations for credentials
- Username and password fields
- Notes field
- SQLite database backend
- Login dialog
- Password generator
- Password strength checker

---

## Future Roadmap

### Planned Features
- [ ] Cloud sync support
- [ ] Mobile companion app
- [ ] Browser extension integration
- [ ] Biometric authentication
- [ ] Password breach checking
- [ ] Secure sharing
- [ ] Multiple vault support
- [ ] Custom fields
- [ ] Tags system
- [ ] Advanced search with filters
- [ ] Backup and restore automation
- [ ] Audit log
- [ ] Two-factor authentication

### Under Consideration
- Password history tracking
- Auto-fill capabilities
- Emergency access
- Organization/team features
- Cross-platform sync
- Hardware key support

---

## Version Numbering

This project uses [Semantic Versioning](https://semver.org/):
- **MAJOR** version for incompatible API/database changes
- **MINOR** version for new features (backward compatible)
- **PATCH** version for bug fixes (backward compatible)

---

**Current Version:** 3.0.0  
**Status:** Production Ready ✅  
**Last Updated:** October 29, 2025
