#!/bin/bash
# Database diagnostic script for SecureVault

echo "=== SecureVault Database Diagnostic ==="
echo ""

DB_PATH="/home/abin/Documents/Secure_Valut/PRoejct/securevault.db"

if [ ! -f "$DB_PATH" ]; then
    echo "❌ Database file not found!"
    exit 1
fi

echo "✓ Database file exists"
echo ""

echo "=== Database Schema ==="
sqlite3 "$DB_PATH" ".schema"
echo ""

echo "=== Users ==="
sqlite3 "$DB_PATH" "SELECT id, username, created_at FROM users;"
echo ""

echo "=== Credentials Count ==="
echo "Total credentials: $(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM credentials;")"
echo "Credentials by user:"
sqlite3 "$DB_PATH" "SELECT users.username, COUNT(credentials.id) as count FROM users LEFT JOIN credentials ON users.id = credentials.user_id GROUP BY users.id;"
echo ""

echo "=== All Credentials (with user) ==="
sqlite3 "$DB_PATH" "SELECT credentials.id, users.username, credentials.title, credentials.username FROM credentials LEFT JOIN users ON credentials.user_id = users.id;"
echo ""

echo "=== Diagnostic Complete ==="
