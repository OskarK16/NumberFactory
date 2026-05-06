# NumberFactory

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Autorzy

Dominik Kaim (grupa 3), Józef Potaczek (grupa 3), Oskar Krawczyk (grupa 3)

## Opis projektu

Pomysł na projekt to gra planszowa 2d która będzie inspirowana grami takimi jak Beltmatic/Factorio/Shapez

Gracz dostaje do dyspozycji:
1. Planszę (wstępnie tylko skończoną, jak się uda to również dowolnie dużą) podzieloną na kwadraty 1x1
2. Zbiór komponentów

Cel gry:
Ułożenie komponentów na planszy w taki sposób aby wygenerować fabrykę która do komponentu "output" dostarczy zadany wcześniej ciąg/zbiór liczbowy (rozważamy również tryb sandbox gdzie takiego celu nie będzie, wtedy też zbiór komponentów będzie nieograniczony)

Przebieg gry:
Po uruchomieniu aplikacji gracz zobaczy proste "menu" skąd będzie mógł wybrać jeden z dwóch trybów gry.
Opcja 1. Tryb "kampanii" 
Gracz układa swoją maszynę po czym po jej ułożeniu wciska przycisk "submit", wtedy maszyna rozpoczyna swoje działanie i jeżeli do komponentu "output" zostaną dostarczone odpowiednie elementy to poziom jest ukończony.
Opcja 2. Tryb "sandbox"
Identycznie jak tryb kampanii jednak tutaj cel gracz wybiera dowolnie i ilość komponentów którymi on dysponuje jest nieograniczona

Komponenty:

Każdy z komponentów będzie "maszyną"/"funkcją" która przyjmuje ustaloną ilość argumentów i "wyrzuca" na wyjście pewne wartości (w zależonści od komponentu)
Każdy komponent składa się z pewnego spójnego podzbioru kafelków planszy. (Na razie planujemy tylko komponenty 1x1)

Przykładowo komponent dodawania może być wielkości 1x1 i jego dwie krawędzie posłużą za input dwóch argumentów a jedna z dwóch pozostałych może być outputem który zwróci sumę argumentów

Wstępna lista komponentów:
1. Komponent generujący - bezargumentowy komponent który wysyła na output stałą liczbę (np co sekundę wyrzuca liczbę 3).
2. Komponent niszczący - przyjmuje input i nie ma żadnych outputów
3. Komponent transportowy - jeden agrument który przekazuje na output bez zmian 
4. Komponenty arytmetyczne - będą realizowały podstawowe operacje arytmetyczne (dodawanie, odejmowanie, mnożenie, dzielenie całkowite, modulo)
5. Komponenty logiczne - będą realizowały operacje logiczne (==, !=, <, <=, >, >=), mają dwa inputy (a, b) i dwa outputy (x, y) dla operacji logicznej 'p' komponent działa tak że jeżeli apb jest prawdą to przekierowuje a na x w przeciwnym przypadku przekierowuje a na y. (dokładne działanie komponentów logicznych jest jeszcze omawiana, to jest wstępny pomysł)
6. Komponent "output" - przyjmuje jeden input i przekazujemy go do celu zadania.

## Technologie
Java 
libGDX
