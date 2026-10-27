# Lab 5: XML con DOM y SAX

Lectura y validación de documentos XML en Java con las dos APIs clásicas.

| Carpeta | Qué hace |
|---|---|
| [DOM](DOM) | `ValidarBOE` carga `boe.xml` con `DocumentBuilder` y comprueba que esté bien formado |
| `SAX` | `ValidatorXerces_J_SAX` lee `Ej01.xml` con un parser SAX que valida el documento contra el DTD que trae dentro |
| `xml` | Un XML de ejemplo |

La diferencia que muestra el lab: DOM carga todo el documento en memoria como un árbol, y SAX lo recorre como una secuencia de eventos sin guardarlo entero.

## Ejecutarlo

```
cd SAX
javac ValidatorXerces_J_SAX.java
java ValidatorXerces_J_SAX
```

Para DOM, ver su [README](DOM).
