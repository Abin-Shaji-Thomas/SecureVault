#!/bin/bash
# Run SecureVault with database support
# For Kali Linux / Debian-based systems

cd "$(dirname "$0")"

# Check if required libraries exist
if [ ! -f "lib/sqlite-jdbc-3.44.1.0.jar" ]; then
    echo "Downloading SQLite JDBC driver..."
    mkdir -p lib
    wget -q -O lib/sqlite-jdbc-3.44.1.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.44.1.0/sqlite-jdbc-3.44.1.0.jar
fi

if [ ! -f "lib/slf4j-api-2.0.9.jar" ]; then
    echo "Downloading SLF4J API..."
    wget -q -O lib/slf4j-api-2.0.9.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar
fi

if [ ! -f "lib/slf4j-simple-2.0.9.jar" ]; then
    echo "Downloading SLF4J Simple..."
    wget -q -O lib/slf4j-simple-2.0.9.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar
fi

# Run with complete classpath - Enhanced Version
echo "🔐 Starting SecureVault Pro - Enhanced Edition..."
echo "✨ Beautiful UI with all features!"
# Put compiled classes (bin) first so the main class is found reliably, then include all jars in lib/
java -cp "bin:lib/*" SecureVaultSwingEnhanced

