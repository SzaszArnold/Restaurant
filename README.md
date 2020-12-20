# Where to eat?
A projekt célja egy olyan applikáció létrehozása amely képes éttermek adatlapjainak a megjelenítésére, ezeket bizonyos szempontok szerint lehet szűrni, illetve a kedveltek listájára lehet tenni. Hozzátartozik még egy felhasználói adatlap, amelybe a felhasználó megadhatja a személyes adatait, profil képet állíthat be magának illetve ezen adatokat meg is változtathatja.
## Felhasznált technológiák
Az applikáció Kotlin nyelven irodott Android oprációs rendszerre. Különböző könyvtárak kerültek felhasználásra a projekt elkészítése során, ezek közül néhány:
- Retrofit, segítségével tudja az applikáció lekérdezni a Rest Apitól az éttermek adatait
- Room adatbázis, itt tárolja el lokálisan az applikáció a profil adatokat illetve a kedvelt éttermek listáját
- Glide, segítségével történik a képek betöltése és szerkeztése
- RecycleView, segítségéve van megoldva az adatok egymásutáni megjelenítése listaként, ide tartozik még a lapozás funkció is.
Az applikáció nagyrészt betartja a Modell-nézet-nézetmodell(MVVM) tervezési minta szabályait, ezért a felhasználói felület külön van választva az adat modelltől.
## Funkciók részletezése
- Splash screen
Ez az ablak fogadja a felhasználót az applikáció indulásakor. Feladata az adatok betöltése. Amig nem sikerül betölteni az adatokat megjelenítésre, addig nem lép tovább a következő felületre.
<img src="https://github.com/SzaszArnold/Restaurant/blob/main/ScreenShoots/Splash.jpg" alt="drawing" width="200"/>
- Home screen
Itt találhatóak az éttermek amelyeket a splash alatt betöltött az applikáció. Itt lehet beállítani a szűrési funkciókat is.
<img src="https://github.com/SzaszArnold/Restaurant/blob/main/ScreenShoots/Home.jpg" alt="drawing" width="200"/>
A szűréseket a következőképpen lehet végezni:
- None azaz nincs szűrés beállítva
- Name: megjelenik egy mező ahova lehet írni, beírva a kulcsszót és megérintve a kereső gombot megjelennek a találatok
- City: megjelenik a városok listája, kiválasztva egyet megjelennek a találatok 
- Price: megjelenik egy kereső mező ahova az árat lehet írni 1-5 között, kereső gombra aktiválodik
- Fav: megjelenik az előzetesen kedvelt étermek listája a keresőgomb lenyomása után.
Az összes megjelenített étteremnek van egy részletesebb infó oldala, amelyet úgy lehet elérni, hogy a felhasználó elérinti az egyik étterem adatlapját.
- Detail screen
Ezen az oldalon lehet megtekíteni az előzetesen kiválasztott étterem részleteit. Lehetőség van megtekínteni a térképen, eléríntve a Map mezőt, fel lehet hívni az éttermet elérintve a telefonszámot(ehez előzetes engedélyadás szükséges), illetve lehet ki-be kedvelni az adott étermet a csillag gombocskával.
<img src="https://github.com/SzaszArnold/Restaurant/blob/main/ScreenShoots/Detail.jpg" alt="drawing" width="200"/>
- ProfileUpdate Screen
Lehetősége van a felhasználónak megadni a a nevét, telefonszámát, címét, email fiókjált, illetve állíthat be profil képet, ezt a profil Screen oldalon meg is tekíntheti.
<img src="https://github.com/SzaszArnold/Restaurant/blob/main/ScreenShoots/ProfileUpdate.jpg" alt="drawing" width="200"/>
További képek a következő [linken](https://github.com/SzaszArnold/Restaurant/tree/main/ScreenShoots) érhetők el.
