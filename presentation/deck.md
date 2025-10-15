---
title: "**cachet**"
sub_title: Dans le cadre de l'unité « Développement d'applications internet »
authors: 
    - Colin Stefani
    - Simão Romano Schindler
theme: 
    name: light
---

Table des matières
===

- Choix du projet
- Fonctionnalités
- Structure du projet
- Détails d'implémentation
- Suite / idées d'améliorations
- Démo et questions

<!-- end_slide -->

Fonctionnalités
===

### Génération de clés Ed25519
Generation and management of Ed25519 cryptographic keys

### Signature de fichiers
Sign files with private keys

### Validation de signature
Verify file signatures with public keys

<!-- end_slide -->

Structure du projet
===

```
src/
└── main/
    └── java/
        └── ch.heigvd/
            ├── commands/
            │   ├── Cachet
            │   ├── Keygen
            │   ├── Sign
            │   └── Verify
            ├── exceptions/
            ├── utils/
            │   ├── FileIOUtils
            │   ├── KeyUtils
            │   └── SignatureUtils
            ├── Constants
            └── Main
└── test/
```

<!-- end_slide -->

Détails d'implémentation
===

- Courbe elliptique ED25519
- Clé privée PKCS#8
- Clé publique SPKI
- Signature Base64
- java.security pour éviter les dépendances externes

<!-- end_slide -->

Suite / idées d'améliorations
===

- Signer des répertoires de fichiers
- Capacité de keygen à dériver une clé publique

<!-- end_slide -->
<!-- font_size: 3 -->
<!-- jump_to_middle -->
Démo et questions
===