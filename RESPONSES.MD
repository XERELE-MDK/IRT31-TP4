## 1. Différence entre @Controller, @RestController et rôle de @ResponseBody
@Controller : Annotation Spring MVC classique
@RestController : Annotation composite spécialisée qui combine @Controller et @ResponseBody.
Rôle de @ResponseBody : Cette annotation indique à Spring MVC de ne pas chercher à résoudre une vue HTML.


## 2. Idempotence des verbes HTTP
   Un verbe HTTP est idempotent si l'exécution répétée de la même requête un nombre quelconque de fois produit exactement le même effet sur l'état des ressources côté serveur que si elle n'avait été exécutée qu'une seule fois.

## 3. Initialisation avec @PostConstruct vs corps de getStudents()
Intérêt de @PostConstruct : Cette méthode s'exécute une seule fois immédiatement après le démarrage du bean Spring et l'injection de ses dépendances. Les données d'initialisation sont ainsi créées ou chargées en mémoire au lancement du service.

Coût de l'initialisation dans getStudents() :

- Effondrement des performances et surcharge mémoire : À chaque requête GET /students, le dataset serait réinstancié ou rechargé à partir de zéro, provoquant des allocations d'objets inutiles et des sollicitations intempestives du Garbage Collector.

- Perte de persistance en mémoire (Effet de bord) : Si des méthodes POST ou DELETE modifient la liste en cours d'exécution, appeler ensuite getStudents() réinitialiserait la liste et effacerait toutes les modifications apportées par les requêtes précédentes.

## 4. Rôle de Jackson et évolutions dans Spring Boot 4
Rôle de Jackson dans ce TP : Jackson est le moteur de sérialisation/désérialisation par défaut de Spring Boot. Il transforme automatiquement les objets Java (DTOs, entités) en chaînes JSON lors de l'envoi de la réponse HTTP, et inversement, convertit le payload JSON reçu dans une requête HTTP en objets Java.
Évolutions de Spring Boot 4 par rapport à Spring Boot 2/3 :
Version : Spring Boot 2 et 3 reposent sur Jackson 2.x. Spring Boot 4 bascule par défaut sur Jackson 3.x.
Packages & Group IDs : Les coordonnées Maven/Gradle et packages Java passent du namespace legacy com.fasterxml.jackson.* au nouveau namespace tools.jackson.* (à l'exception du module jackson-annotations qui conserve com.fasterxml.jackson.annotation par rétrocompatibilité). 
API & Classes : JsonMapper (construit via un builder/customizer) remplace l'usage direct de ObjectMapper, et les classes d'intégration Spring évoluent en conséquence (par exemple via JsonMapperBuilderCustomizer). De plus, les dates sont désormais sérialisées au format ISO-8601 par défaut.



## 5. Utilité de ResponseEntity
Par rapport à un simple retour d'objet Java (qui renvoie toujours le payload avec un code HTTP 200 par défaut), ResponseEntity représente l'intégralité de la réponse HTTP et permet un contrôle fin sur la communication avec le client.

## 6. Sélection du @ExceptionHandler par Spring

Lorsqu'une exception est levée durant le traitement d'une requête, Spring MVC (ExceptionHandlerExceptionResolver) applique l'algorithme suivant :

- Recherche de portée (Scope) : Spring cherche d'abord s'il existe une méthode @ExceptionHandler à l'intérieur du contrôleur local où l'exception s'est produite. S'il n'en trouve pas, il recherche parmi les beans globaux annotés @ControllerAdvice / @RestControllerAdvice.

- Correspondance exacte (Exact Match) : Il vérifie si une méthode est annotée avec le type exact de l'exception levée (ex: @ExceptionHandler(StudentNotFoundException.class)).

- Hiérarchie d'héritage la plus proche (Nearest Superclass Match) : Si aucune correspondance exacte n'existe, Spring remonte l'arbre d'héritage Java de l'exception et sélectionne le handler dont la classe gérée est la sous-classe la plus proche dans la hiérarchie.

- Ordre des Advice : S'il y a conflit entre plusieurs @ControllerAdvice, Spring utilise la priorité définie via l'annotation @Order ou l'interface Ordered.

## 7. Résolution du problème par @ControllerAdvice et restriction de sa portée
Problème résolu : Un @ExceptionHandler déclaré dans un contrôleur ne traite que les exceptions de ce seul contrôleur. @ControllerAdvice (ou @RestControllerAdvice) permet de centraliser la gestion globale des exceptions pour toute l'application
évitant la duplication du code de gestion d'erreurs.
Cas de restriction de sa portée :Dans une application volumineuse ou hybride composée de modules différents 
Moyens de restriction :

- Par package : @ControllerAdvice(basePackages = "com.example.api")

- Par type d'annotation : @ControllerAdvice(annotations = RestController.class)

- Par classe : @ControllerAdvice(assignableTypes = StudentController.class)

## 8. Avantages d'un code 404 + JSON structuré vs IndexOutOfBoundsException (500)

Sémantique et clarté du statut HTTP :

- Le code 404 Not Found signale explicitement que la ressource demandée n'existe pas (erreur côté client / mauvaise requête).

- Le code 500 Internal Server Error signale un bug non géré du serveur, induisant le consommateur en erreur en lui laissant croire que l'API est en panne.


