# Cachet

CLI tool for cryptographic signing and verification of digital documents using Ed25519.

Developed by Colin Stefani and Simão Romano Schindler, as part of the teaching unit 
"Développement d'applications internet (DAI)" at HEIG-VD.

## Features
- Ed25519 private/public key generation
- File signing
- Signature verification

One command-line interface (CLI) for all operations.

---

## Installation

### Prerequisites
- Java 21+

### Build
```sh
./mvnw clean package
```
The jar will be generated in `target/cachet-1.0-SNAPSHOT.jar`.

---

## Usage

### Key Generation
```sh
java -jar target/cachet-1.0-SNAPSHOT.jar keygen private.pem public.pem
```
- `private.pem`: Ed25519 private key (PEM, PKCS#8)
- `public.pem`: Ed25519 public key (PEM, SubjectPublicKeyInfo)

### File Signing
```sh
java -jar target/cachet-1.0-SNAPSHOT.jar sign <file_to_sign> <signature_out.sig> <private.pem>
```
- The signature is Base64-encoded and written to `<signature_out.sig>`

### Signature Verification
```sh
java -jar target/cachet-1.0-SNAPSHOT.jar verify <file_to_verify> <signature.sig> <public.pem>
```
- The signature is read, decoded from Base64, and verified

### Full Example
```sh
# Generate keys
java -jar target/cachet-1.0-SNAPSHOT.jar keygen --private private.pem --public public.pem

# Sign a file
java -jar target/cachet-1.0-SNAPSHOT.jar sign text.txt output.sig private.pem

# Verify the signature
java -jar target/cachet-1.0-SNAPSHOT.jar verify text.txt output.sig public.pem
```

---

## Project Structure

```
cachet/
├── src/
│   ├── main/java/ch/heigvd/commands/   # CLI commands (Sign, Verify, Keygen)
│   ├── main/java/ch/heigvd/utils/      # Utilities (SignatureUtils, FileIOUtils)
│   └── main/java/ch/heigvd/SignatureConstants.java
├── test/java/ch/heigvd/commands/       # Unit tests
└── README.md
```

---

## Implementation details

### Algorithm
- Ed25519 (Edwards-curve Digital Signature Algorithm)
- Algorithm configured in `SignatureConstants.java` (default: Ed25519)

### Key Format
- Keys in PEM format

### Signature Format
- Binary signature encoded in Base64 for storage and transport

---

## Tests
- Run unit tests with:
```sh
./mvnw test
```
