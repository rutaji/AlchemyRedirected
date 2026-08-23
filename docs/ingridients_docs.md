
## Příklad

ingredients:
  CARROT:
    toxicity: 10
    loreLevel: 3
    exp: 6
    effects:
      - type: NIGHT_VISION
        value: 20
        max: 1
      - type: SATURATION
        value: 9
        max: 1
  POTATO:
    toxicity: 30
    loreLevel: 1
    exp: 67
    effects:
      - type: LIFESTEAL
        value: 60
        max: 3
      - type: SATURATION
        value: 60
        max: 2
baseDuration: 3600
levelDuration: 600
levelDurationUnaffectedByLevels: 2400
lifestealPerLevel: 0.1
bombPowerPerLevel: 3.0

## Ingredience

ingredients
    - pole ingrediencí
    - každá ingredience musí být id minecraft předmětu 
        - id se dají najít na wiki, ale většinou jsou stejná jako jména
        - použijte _ místo mezery
        - chybné id budou přeskočeny při registraci
        - lze používat malá a velká písmena, ale kvúli konzistenci bych doporučil používat velká písmena

každá ingredience musí obsahovat následující staty, jinak budou nahrazeny default value neboli nulou

toxicita
    - kolik daný předmět přidá toxicity (bude procentně sníženo podle alchemy levelu hráče)
    - musí být celé číslo
    - počítal jsem s tím že bude větší nebo rovno 0 (můžu změnit)

lorelevel
    - jaký musí mít hráč alchemy level aby viděl informace o ingrediency
    - celé číslo
    - větší nebo rovno 0
    - pokud je nastaven na 0 jsou informace odemknuty všem hráčům od začátku

exp
    - kolik exp dostane hráč za použití ingredience
    - desetiné číslo

effects
    - pole obsahující potion effect ingredience
    - pole může být prázdné, ingredience poté zmizí při hození do kotle ale nepřidá žádný effect, pořád ale může dávet exp a toxicitu
    - každý effect mít následující 3 položky: type, value, max

type 
    - id effectu
    - může být custom effect anebo vanilla effect
    - použijte _ místo mezery
    - id se dají najít na wiki, ale většinou jsou stejná jako jména
    - MUSÍ být napsáno VELKÝMI PÍSMENY

value
    - kolik bodů přidá 1 kus ingredience k effektu za 100 se zvedne effekt o level
    - celé číslo
    - počítal jsem že to bude větší než 0 ale lze změnit

max 
    - pokud je effekt v cauldronu rovno nebo větší než max, tato ingredience už nepřidává body za tuto hranici
    - pro každý effekt řešeno samostatně => ostatní effekty se normálně přidají
    - nepř pokud je max 2 tak nemůže daný effekt zvýšit na víc než 200 bodů
    - celé číslo větší než 0

## Ostatní




## seznam effektů

### duration 
LIFESTEAL
BOMB

### instant
SMOKE
CLEANSE
