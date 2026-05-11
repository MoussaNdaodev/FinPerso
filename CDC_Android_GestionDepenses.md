# 📱 Cahier des Charges — Application Android Java
## Gestion des Dépenses Personnelles

| Champ | Détail |
|---|---|
| **Référence** | CDC-TP-ANDROID-DEPENSES-2025 |
| **Version** | 1.0 — Édition Travaux Pratiques |
| **Date** | Avril 2025 |
| **Plateforme** | Android (Java) — API 21+ |
| **Nature** | Travaux Pratiques Académique |
| **Délai** | 12 mai 2026 à 23h59 |
| **Organisation** | Par groupe de 4 étudiants |
| **Enseignant** | M. Banga, Ingénieur Logiciel |

---

## 1. Contexte et Objectifs

### 1.1 Contexte
Application Android native en Java permettant à un utilisateur de **suivre ses finances personnelles** de manière simple et intuitive. Toutes les données sont stockées **localement** sur l'appareil via SQLite.

### 1.2 Objectifs Pédagogiques
- Maîtriser le développement d'une application Android native en Java
- Implémenter la persistance locale avec SQLite (via Room)
- Appliquer le pattern architectural **MVC ou MVVM**
- Gérer les interactions utilisateur avec les composants Android standards
- Concevoir et manipuler une base de données relationnelle embarquée

### 1.3 Périmètre

| ✅ Inclus dans le TP | ❌ Exclu du TP |
|---|---|
| Saisie et consultation des dépenses | Synchronisation avec un serveur distant |
| Saisie et consultation des revenus | Authentification / compte utilisateur |
| Gestion des catégories et rubriques | Export PDF ou CSV |
| Définition de plafonds et budgets | Graphiques et statistiques avancées |
| Calcul du solde disponible | Notifications push |
| Interface simple et fonctionnelle | Mode hors-ligne spécifique |

---

## 2. Spécifications Fonctionnelles

### 2.1 Module — Gestion des Dépenses

#### Saisie d'une dépense
| Champ | Type | Obligatoire |
|---|---|---|
| Montant | Décimal | ✅ |
| Catégorie | Liste déroulante | ✅ |
| Rubrique / sous-catégorie | Liste déroulante | ❌ |
| Date | Date (défaut : aujourd'hui) | ✅ |
| Description / note | Texte libre | ❌ |
| Moyen de paiement | Espèces / Mobile Money / Carte / Autre | ❌ |

#### Consultation et gestion
- Afficher la liste de toutes les dépenses (tri par date, décroissant)
- Modifier une dépense existante
- Supprimer une dépense (avec confirmation)
- Filtrer par période : jour, semaine, mois en cours
- Filtrer par catégorie

---

### 2.2 Module — Gestion des Revenus

#### Saisie d'un revenu
| Champ | Type | Obligatoire |
|---|---|---|
| Montant | Décimal | ✅ |
| Source | Salaire / Commerce / Freelance / Don / Autre | ✅ |
| Date | Date (défaut : aujourd'hui) | ✅ |
| Description | Texte libre | ❌ |

#### Calcul du Solde Disponible
> **Solde = Total Revenus − Total Dépenses** (sur la période sélectionnée)

- Affichage permanent du solde sur l'écran principal
- Indicateur visuel : 🟢 solde positif | 🔴 solde négatif

---

### 2.3 Module — Catégories et Rubriques

#### Catégories prédéfinies

| Catégorie | Exemples de Rubriques |
|---|---|
| 🍽️ Alimentation | Restaurant, Marché, Épicerie, Fast-food |
| 🚗 Transport | Taxi, Bus, Carburant, Parking |
| 🏠 Logement | Loyer, Électricité, Eau, Internet |
| 🏥 Santé | Pharmacie, Consultation, Hôpital |
| 📚 Éducation | Frais scolaires, Fournitures, Livres |
| 🎭 Loisirs | Divertissement, Sport, Voyage |
| 👗 Habillement | Vêtements, Chaussures, Accessoires |
| 📦 Autre | Divers |

#### Gestion des catégories (CRUD)
- Créer une nouvelle catégorie (nom + icône ou couleur)
- Modifier le nom d'une catégorie personnalisée
- Supprimer une catégorie (**si aucune dépense associée**)
- Ajouter des rubriques à une catégorie existante

---

### 2.4 Module — Plafonds et Budgets

#### Définition des plafonds

| Type | Description | Exemple |
|---|---|---|
| Par catégorie | Montant max sur le mois pour une catégorie | Max 50 000 FCFA/mois en Alimentation |
| Global mensuel | Budget total toutes catégories confondues | Budget total : 200 000 FCFA/mois |

#### Suivi et alertes visuelles

| Seuil | Couleur |
|---|---|
| < 70% | 🟢 Vert |
| 70% – 90% | 🟠 Orange |
| > 90% | 🔴 Rouge |

- Barre de progression : montant consommé / plafond défini
- Affichage du montant restant disponible par catégorie
- Message d'alerte si le plafond est dépassé

---

### 2.5 Tableau de Bord (Écran Principal)
- Solde disponible du mois (Revenus − Dépenses)
- Total des dépenses du mois en cours
- Liste des **5 dernières transactions**
- Bouton d'accès rapide à la saisie d'une dépense
- Résumé des plafonds : catégories dont le budget est critique

---

## 3. Écrans et Navigation

### 3.1 Bottom Navigation Bar (4 onglets)

| Onglet | Contenu |
|---|---|
| 🏠 Accueil | Dashboard : solde, dernières transactions, alertes budgets |
| 💸 Dépenses | Liste des dépenses + bouton ajout (FAB) |
| 💰 Revenus | Liste des revenus + bouton ajout (FAB) |
| 📊 Budgets | Plafonds par catégorie + budget global |

### 3.2 Liste des Écrans

| Écran | Description |
|---|---|
| Splash Screen | Écran de démarrage avec nom de l'app (2 secondes) |
| Dashboard | Solde du mois, top 5 transactions, alertes plafonds |
| Liste Dépenses | RecyclerView avec filtres période/catégorie, FAB d'ajout |
| Formulaire Dépense | Saisie : montant, catégorie, rubrique, date, note, moyen paiement |
| Détail Dépense | Affichage + boutons Modifier / Supprimer |
| Liste Revenus | RecyclerView des revenus, FAB d'ajout |
| Formulaire Revenu | Saisie : montant, source, date, description |
| Gestion Budgets | Liste des plafonds par catégorie avec barres de progression |
| Formulaire Budget | Définition : catégorie + montant + période |
| Gestion Catégories | Liste des catégories + rubriques, ajout/modification |
| Paramètres | Devise, nom utilisateur, réinitialisation données |

---

## 4. Spécifications Techniques

### 4.1 Stack Technologique

| Composant | Technologie | Détails |
|---|---|---|
| Langage | Java | Java 8 minimum |
| IDE | Android Studio | Version Hedgehog ou supérieure |
| SDK minimum | API 21 | Android 5.0 Lollipop |
| SDK cible | API 34 | Android 14 |
| Base de données | SQLite via Room | Room 2.x (ORM Android officiel) |
| Architecture | MVVM (recommandé) ou MVC | ViewModel + LiveData |
| Navigation | Bottom Navigation Bar | Fragments ou Activities |
| UI | Material Design Components | RecyclerView, CardView, FAB, etc. |
| Async | AsyncTask ou LiveData | Pour les opérations SQLite |

### 4.2 Structure des Packages

```
com.tp.gestiondepenses/
├── model/          → Entités : Depense, Revenu, Categorie, Rubrique, Budget
├── database/       → AppDatabase, DAOs (DepenseDao, RevenuDao, etc.)
├── repository/     → DepenseRepository, RevenuRepository, BudgetRepository
├── viewmodel/      → DashboardViewModel, DepenseViewModel, BudgetViewModel
├── ui/             → Activities et Fragments (Dashboard, Depenses, Revenus, Budgets)
├── adapter/        → RecyclerView Adapters
└── utils/          → DateUtils, CurrencyUtils, Constants
```

### 4.3 Composants Android Utilisés
- **Activity** : `MainActivity` (conteneur de navigation)
- **Fragments** : `DashboardFragment`, `DepensesFragment`, `RevenusFragment`, `BudgetsFragment`
- **RecyclerView + Adapter** : pour les listes de transactions
- **ViewModel + LiveData** : pour la réactivité des données
- **Room Database** : avec `AppDatabase` et les DAOs
- **AlertDialog** : pour les confirmations (suppression)
- **DatePickerDialog** : pour la sélection de la date
- **Spinner** : pour les sélecteurs (catégorie, moyen de paiement)

---

## 5. Modèle de Données (Room / SQLite)

| Table (Entité) | Clé Primaire | Champs principaux |
|---|---|---|
| `categories` | id (int, autoincrement) | nom, icone, couleur, est_defaut (boolean) |
| `rubriques` | id (int, autoincrement) | categorie_id (FK), nom |
| `depenses` | id (int, autoincrement) | categorie_id (FK), rubrique_id (FK nullable), montant, date, description, moyen_paiement, created_at |
| `revenus` | id (int, autoincrement) | source, montant, date, description, created_at |
| `budgets` | id (int, autoincrement) | categorie_id (FK nullable = global), montant_plafond, periode, mois, annee |

### 5.1 Méthodes DAO Requises

| DAO | Méthodes |
|---|---|
| `DepenseDao` | `insertDepense()`, `updateDepense()`, `deleteDepense()`, `getAllDepenses()`, `getDepensesByMois(mois, annee)`, `getDepensesByCategorie(cat_id)`, `getTotalDepensesParMois(mois, annee)`, `getTotalParCategorie(cat_id, mois, annee)` |
| `RevenuDao` | `insertRevenu()`, `updateRevenu()`, `deleteRevenu()`, `getAllRevenus()`, `getRevenusByMois(mois, annee)`, `getTotalRevenusParMois(mois, annee)` |
| `CategorieDao` | `insertCategorie()`, `updateCategorie()`, `deleteCategorie()`, `getAllCategories()`, `getCategorieById(id)` |
| `BudgetDao` | `insertBudget()`, `updateBudget()`, `deleteBudget()`, `getBudgetByCategorie(cat_id, mois, annee)`, `getAllBudgets()`, `getBudgetGlobal(mois, annee)` |

---

## 6. Règles Métier

### 6.1 Calcul du Solde
> **Solde du mois = Σ revenus du mois − Σ dépenses du mois**
> Le mois de référence est le mois calendaire en cours (1er au dernier jour du mois).

### 6.2 Gestion des Plafonds
- Plafond défini par **mois calendaire** (non glissant)
- Pourcentage de consommation = `(dépenses catégorie / plafond) × 100`
- Reliquat = `plafond − dépenses de la période`
- Si aucun plafond défini → pas de barre de progression
- Le budget global est **indépendant** des budgets par catégorie

### 6.3 Validations Obligatoires
- Montant **strictement supérieur à 0**
- La date **ne peut pas être dans le futur** (dépenses et revenus)
- Le plafond défini **doit être supérieur à 0**
- Une catégorie **ne peut pas être supprimée** si des dépenses lui sont associées

---

## 7. Exigences d'Interface

### 7.1 Principes Généraux
- Langue : **Français**
- Design : **Material Design** (`com.google.android.material`)
- Devise par défaut : **FCFA (XOF)** (configurable dans les paramètres)
- Format de date : **JJ/MM/AAAA**
- Support **portrait uniquement** (paysage non requis)

### 7.2 Composants UI Obligatoires

| Composant | Utilisation |
|---|---|
| `RecyclerView` | Toutes les listes : dépenses, revenus, catégories, budgets |
| `CardView` | Chaque élément de liste + cartes du dashboard |
| `FloatingActionButton (FAB)` | Ajout d'une dépense ou d'un revenu |
| `ProgressBar horizontale` | Suivi des plafonds budgétaires |
| `Spinner (Dropdown)` | Sélection catégorie, rubrique, moyen de paiement |
| `TextInputLayout (Material)` | Tous les champs de saisie |
| `BottomNavigationView` | Navigation entre les 4 sections |
| `Snackbar` | Messages de confirmation (suppression, sauvegarde) |
| `AlertDialog` | Confirmation avant suppression |

---

## 8. Livrables et Évaluation

### 8.1 Livrables Attendus
1. Code source complet du projet Android Studio (zippé)
2. APK de l'application signé en mode debug
3. Rapport technique (diagramme de classes, schéma de base de données)
4. Manuel d'utilisation avec captures d'écran des écrans principaux

### 8.2 Critères d'Évaluation

| Critère | Points | Description |
|---|---|---|
| CRUD Dépenses | 20 pts | Ajout, affichage, modification, suppression fonctionnels |
| CRUD Revenus | 15 pts | Ajout, affichage, modification, suppression fonctionnels |
| Gestion catégories et rubriques | 15 pts | Catégories prédéfinies + création personnalisée |
| Plafonds et budgets avec barres de progression | 20 pts | Définition, calcul du taux, code couleur |
| Dashboard avec solde disponible | 10 pts | Calcul correct, affichage clair |
| Qualité du code et architecture | 10 pts | Structure propre, séparation des responsabilités |
| Interface utilisateur (Material Design) | 10 pts | Navigation claire, cohérence visuelle |
| **TOTAL** | **100 pts** | |

### 8.3 Contraintes Techniques Obligatoires
- ✅ Utilisation **obligatoire de Room** pour la persistance (SQLite brut interdit)
- ✅ **Aucune donnée hardcodée** dans les vues
- ✅ Respect des conventions de nommage Java (CamelCase)
- ✅ L'application **ne doit pas crasher** sur les parcours utilisateurs principaux
- ✅ Le **filtre par mois** sur les dépenses et revenus doit fonctionner correctement

---

## 9. Planning Suggéré (21h)

| Séance | Contenu | Durée |
|---|---|---|
| 1 | Setup projet Android Studio, création de la base Room, entités et DAOs | 3h |
| 2 | Formulaires de saisie : dépenses et revenus (avec validation) | 3h |
| 3 | RecyclerView pour les listes, filtres par période et catégorie | 3h |
| 4 | Module catégories et rubriques (CRUD) | 3h |
| 5 | Module plafonds : définition, calcul de progression, code couleur | 3h |
| 6 | Dashboard (solde, alertes, résumé) + navigation finale | 3h |
| 7 | Tests, corrections de bugs, finitions UI | 3h |
| **Total** | | **21h** |

---

## 10. Dépendances Gradle

```groovy
dependencies {
    // Room (ORM SQLite)
    implementation 'androidx.room:room-runtime:2.6.1'
    annotationProcessor 'androidx.room:room-compiler:2.6.1'

    // ViewModel et LiveData
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-livedata:2.7.0'

    // Material Design
    implementation 'com.google.android.material:material:1.12.0'

    // RecyclerView et CardView
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'androidx.cardview:cardview:1.0.0'

    // Navigation Component (optionnel, pour les Fragments)
    implementation 'androidx.navigation:navigation-fragment:2.7.7'
    implementation 'androidx.navigation:navigation-ui:2.7.7'
}
```

---

## 11. Glossaire

| Terme | Définition |
|---|---|
| **CRUD** | Create, Read, Update, Delete — opérations de base sur les données |
| **Room** | Librairie Android officielle (Jetpack) fournissant un ORM au-dessus de SQLite |
| **DAO** | Data Access Object — interface définissant les requêtes sur la base de données |
| **ViewModel** | Composant Android qui conserve et gère les données liées à l'UI |
| **LiveData** | Conteneur de données observable respectant le cycle de vie Android |
| **FAB** | Floating Action Button — bouton d'action flottant circulaire |
| **Plafond** | Montant maximum autorisé pour une catégorie sur une période donnée |
| **Solde disponible** | Total revenus − Total dépenses sur la période de référence |
| **FCFA** | Franc CFA (XOF) — devise utilisée en Afrique de l'Ouest |
| **Material Design** | Système de design de Google pour les applications Android |

---

*Document généré à partir du CDC TP Android Java — v1.0 — Avril 2025*
