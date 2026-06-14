# NumberFactory

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Autorzy

Dominik Kaim, Józef Potaczek, Oskar Krawczyk (*Grupa 3*)

## Opis projektu

Inspiracją do stworzenia projektu były gry planszowe 2D takie jak Shapez, czy Factorio.

W obecnej formie, w grze dostępne są dwa tryby rozgrywki 
1. Sandbox - stanowi miejsce do eksperymentowania i dowolnego zapoznawania się z grą. Liczba dostępnych komponentów, stopnia zaawansowania
budowanych algorytmów jest niemal nieograniczona. Użytkownik ma do dyspozycji również narzędzia takie jak **Sidebar**, pozwalający
na śledzenie postawionych komponentów i działania algorytmów. 
2. Campaigns - stanowi zbiór misji, z którymi użytkownik może się zmierzyć. Wśród nich znajdują się poziomy o różnym poziomie trudności, począwszy od tych pozwalających
na zapoznanie się szerzej z funkcjonalnościami, czy sterowaniem grą, jak i te zaawansowane wymagające analitycznego myślenia i układaniu rozbudowanych algorytmów takich jak np. *Collatz*.

### Przebieg gry
Po uruchomieniu gry gracz widzi intuicyjne **MENU**, z którego może wybrać jeden z dostępnych trybów gry, instrukcję sterowania oraz możliwość wyjścia z aplikacji. W obu trybach sterowanie rozgrywką jest niemal identyczne
1. Sandbox - gracz układa dowolny algorytm, korzystając z dowolnej ilości komponentów różnego rodzaju. Po ułożeniu docelowego algorytmu może śledzić jego działanie korzystając z mechaniki **SIDEBAR**.
2. Campaigns - gracz po lewej stronie ekranu w zakładce **TASK** użytkownik widzi opis zadania do zrealizowania. Po ułożeniu docelowego algorytmu może śledzić progress w realizacji zadania w tej samej zakładce.

W obu przypadkach użytkownik może w dowolnym momencie zatrzymać działanie algorytmu, zresetować jego działanie (rozpoczęcie tego algorytmu od początku), a także zrestartować planszę do stanu początkowego. 

### Komponenty

Każdy z komponentów jest funkcją, która przyjmuje ustaloną ilość argumentów na wejścia oraz zwraca na wyjście pewne wartości (w zależności od komponentu). 
Każdy komponent składa się z pewnego spójnego podzbioru kafelków planszy, obecnie dostępne są tylko komponenty zajmujące kafelek 1x1 czyli podstawową jednostkę planszy.

### Lista komponentów:
1. **Komponent generujący** - bezargumentowy komponent który wysyła na wyjście swoją stałą liczbę. Ma to miejsce jednorazowo w całym działaniu algorytmu.
2. **Komponent niszczący** - przyjmuje wejście, nie wysyła niczego na wyjście. Służy do likwodowania danego elementu w algorytmie.
3. **Komponent transportowy** - przyjmuje argumenty na wejście, a następnie bez żadnych ich modyfikacji przekazuje je dalej w trakcie działania algorytmu.
4. **Komponenty arytmetyczne** - realizują podstawowe operacje arytmetyczne, takie jak dodawanie, odejmowanie, mnożenie, dzielenie całkowite i modulo.
5. **Komponenty logiczne** - realizują operacje logiczne (**==, !=, <, <=, >, >=**), mają dwa wejścia (a, b) i dwa wyjścia (x, y) dla operacji logicznej 'p' komponent działa tak, że jeżeli apb jest prawdą to przekierowuje a na x; w przeciwnym przypadku przekierowuje a na y.
6. **Komponent wyjścia** - przyjmuje jedno wejście i przekazujemy go do celu zadania.
7. **Komponent neutralny** - pasywna przeszkoda bez portów, nie przyjmuje ani nie wysyła wartości, służy do blokowania ścieżek na planszy.
8. **Komponent kopiujący** - jedno wejście i dwa wyjścia, duplikuje przyjętą wartość na oba wyjścia.

### Pomoc w grze:
Z poziomu ekranu gry w obu trybach dostępny jest panel **GUIDE** (po prawej stronie ekranu), który dokuje się po prawej stronie. Dzięki temu można czytać
opisy i jednocześnie testować maszynę. Panel pokazuje kategorie komponentów, takich jak **Utility, Arithmetic, Logic** z ogólnymi opisami,
natomiast po wejściu w daną kategorię - listę komponentów z grafikami i szczegółowym opisem. Podobna zakładka **Ports** objaśnia działanie 
portów wraz z legendą kolorów (niebieskie porty A mają priorytet, czerwone B są z kolei drugorzędne).

Poza tym, sterowanie obowiązujące w naszej grze omówione jest już na pierwszym wyświetlanym ekranie jako **INSTRUCTIONS**, w której użytkownik może się z nim zapoznać.

Dodatkowo, w grze obowiązuje system zapisu obecnego stanu rozgrywki w obu trybach gry. Po dokonaniu zapisu użytkownik może w dowolnym momencie do niego powrócić, odczytując
pozostawiony stan rozgrywki, a następnie dowolnie go modyfikować.

## Przyszłość projektu
W najbliższej przyszłości poza już zaimplementowanymi aspektami naszego projektu, planujemy wprowadzić szereg zmian pozwalających na jeszcze większą satysfakcję i stopień rozwoju rozgrywki.
1. Customizacja komponentów - obecnie dostępne są tylko najbardziej podstawowe komponenty, z pomocą których można zbudować
tak właściwie dowolny algorytm iteracyjny. Planujemy jednak umożliwić również tworzenia czegoś w rodzaju skrótów na zbudowane już struktury,
aby w dalszej części użytkownik mógł wykorzystywać je bez potrzeby repetycji zarówno na obecnej, jak i innych, niezależnych projektach/planszach.
2. Customizacja płytek - obecnie każda płytka (rozumiana jako kwadrat 1x1 na naszej planszy) umożliwia postawienie dowolnych komponentów.
W dalszych etapach naszej pracy chcielibyśmy umożliwić, jak i wymuszać na użytkownikach w trybach kampanii, aby na danym bloku był **obowiązek**, bądź **zakaz** położenia komponentu danego typu.
3. Dalszy rozwój trybu kampanii - aktualnie znajduje się w nim niewiele misji, co z czasem również planujemy rozwinąć i zwiększyć ich liczbę. 

## Technologie
Java 
libGDX
