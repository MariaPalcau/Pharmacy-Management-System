## Sistem de gestiune pentru farmacie
### Palcau Maria-Magdalena

## Descriere
Proiectul are ca scop digitalizarea proceselor de administrare a unei farmacii. Sistemul permite gestionarea produselor (medicamente), comenzilor de marfa, facturilor si rapoartelor.
Aplicatia faciliteaza stocarea si actualizarea datelor despre produse, crearea de comenzi pentru reincarcarea stocurilor, emiterea facturilor pentru vanzari, precum si generarea de rapoarte privind activitatea farmaciei.

## Obiective
- gestionarea a stocului de medicamente
- gestionarea vanzarilor cu clientul
- administrarea comenzilor si facturilor
- generarea rapoartelor de activitate
- controlul accesului in functie de rol

## Arhitectura
![clase UML](documentatie-ghid-utlizare-raport/PharmacyManagementSystem-UMLclasses.jpeg)

## Functionalitati/Exemple utilizare
![cazuri de utilizare UML](documentatie-ghid-utlizare-raport/PharmacyManagementSystem-UMLusecases.jpeg)

- Farmacist
1. Autentificare (Login): farmacistul se autentifica in sistem folosind numele de utilizator si parola.
2. Gestionarea stocului (Manage Stock): permite adaugarea, modificarea, stergerea si cautarea produselor din farmacie.
3. Gestionarea comenzilor (Manage Orders): permite adaugarea, modificarea, stergerea si cautarea comenzilor de la furnizori.
4. Gestionarea facturilor (Manage Invoice Records): adaugarea si cautarea facturilor existente.
5. Generarea facturilor (Generate Bill): creeaza o factura noua pentru o vanzare.
6. Vizualizarea rapoartelor (View Reports) Genereaza rapoarte despre medicamente expirate sau aproape de data de expirare si produse cu stoc limitat.
   
- Manager
1. Autentificare (Login): managerul se conecteaza la sistem pentru a accesa functiile administrative.
2. Aprobarea comenzilor (Approve Orders) Permite managerului sa aprobe sau sa respinga comenzile create de farmacist.
3. Vizualizarea rapoartelor (View Reports) Genereaza rapoarte despre vanzarile in cursul zilei si a lunei, despre cele mai cumparate medicamente sau despre activitatea farmacistilor.
4. Vizualizarea stocului (View Stock) Ofera o imagine de ansamblu asupra stocurilor.

#### Resurse
Markdown Guide, [Online] Available: https://www.markdownguide.org/basic-syntax/ [accesed: Mar 14, 1706]
