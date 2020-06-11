# ProCampo
Ayudamos al agricultor, todo en la palma de su mano.

# PRESENTACIÓN DEL PROYECTO

## Idea
La idea de mi aplicación nace por la necesidad que tiene un agricultor de agilizar todos los tramites y papeleos que la agricultura conlleva.
En los últimos tiempos al agricultor se le ha pedido que tenga un montón de papeleo en regla; lo cual resulta bastante molesto y pesado.
De esa necesidad nace ProCampo

## Descripción general del proyecto
Con ProCampo vas a poder tener ordenados en la palma de tu mano todos esos papeles que tanto tu cooperativa como demás organismos te exigen.
Ademas vas a poder llevar un control de todo lo que al campo respecta (aceitunas cogidas, fertilizantes recomendados).

## Objetivo
El objetivo principal de esta aplicación nativa hecha en Android es seguir con la digitalización de la agricultura, en este caso en la olivicultura.
Desde siempre se ha dicho que hay una brecha tecnológica en el campo, aunque esto es claramente falso ya que la agricultura es uno de los sectores más tecnificados en la actualidad; nada tiene que ver coger aceitunas a día de hoy a como se hacía antaño.

## Público al que va dirigido
Cualquier persona que tenga un cultivo de olivos (sea una pequeña o gran explotación). <i>Tambien se puede utilizar en todo tipo de cultivos agricolas, aunque habrá funciones que no serán de mucha relevancia</i>

## Aspectos técnicos y estéticos
En cuanto a como va a ser la aplicación opto por un diseño claro e intuitivo a uno muy complejo y sea dificil de utilizar para el sector.
Se la sensación que es en el campo realizar las cosas rápidos; y que la cosa "cunda" por eso en ProCampo se intenta que con el mínimo de toque tengamos una experiencia reconfortante y completa.

En aspectos técnicos contaria con partes como un login con todas las opciones de inicio de sesión y registro, control en mis olivares, recolectas; consultas de fertilizantes, sulfatos y abonos de interes. Para ello vamos a utilizar herramientas tales como Android Studio o Firebase

En este repositorio se irá actualizando la información de la APP ProCampo así como se irá subiendo código poco a poco

📹[Vídeo explicativo](https://youtu.be/ZRGfduFV4BE)

# TUTORIAL DE USO DE LA APLICACION 


# APLICACIÓN (ANDROID)

# MÓDULO DE SISTEMAS DE GESTIÓN EMPRESARIAL
Se puede encontrar un documento PDF donde se explica todo lo relacionado con dicho módulo.
📃[PDF MODULO SGE](https://github.com/ivanperezmolina/ProCampo/blob/master/Proyecto%20ProCampo%20Modulo%20SGE.pdf)

# MÓDULO DE DESARROLLO DE INTERFACES




# DIARIO SEMANAL DE LA EVOLUCIÓN DEL PROYECTO
## VIERNES 27 DE MARZO DEL 2020
En esta primera semana he decidido crear un nuevo archivo de proyecto; para no liarme con el otro y demás. Esta semana ha estado dedicada al diseño y elaboración del login y registro; cogiendo el que ya tenía y servía y añadiendo nuevas funcionalidades tales como la autenticación con una cuenta de Google y con tu cuenta de Facebook; queda retocar algunos fallos que esta dando e implementar otra forma de inicio de sesión; que no se si será Twitter o por SMS. 
#### Bibliografía de esta semana
Me he basado para el inicio de sesión por Facebook en el vídeo "Login con Facebook" de Alvarez Tech [Vídeo aquí](https://www.youtube.com/watch?v=1HgM_vc-rSc&t=)

## VIERNES 3 DE ABRIL DEL 2020
Menú principal de la aplicación con un menú lateral implementado. También he elaborado la parte de perfil; aunque ahora solo coja la información del usuario (tendría que ver desde donde inicia sesión; por ejemplo si es desde Facebook no va a poder cambiar sus datos)
Una de las partes de la APP ; es decir "Cultivos" ya tiene creada su Adaptador y su Recycler View; solo quedan unos fallos que da al conectar a Firebase y esa parte ya estaría 

#### Bibliografía de esta semana
Ya que la herramienta de Android Studio para crear un NavigationDrawer mete código el cual veo que no sirve he seguido un tutorial de como crearlo desde 0. [Página web aquí](http://umhandroid.momrach.es/basicnavigationdrawer/)

## VIERNES 10 DE ABRIL DEL 2020 (VIERNES SANTO)
Durante esta semana me tomo un respiro

## VIERNES 17 DE ABRIL DEL 2020
En esta semana he estado pensando las diferentes partes de la aplicación y hablando con varios agricultores que conozco para que me puedan dar ideas y demás. Saco dos ideas claves; un foro y un diccionario de fitosanitarios. Buenas ideas!
En cuanto a la aplicación he corregido un error que me daba con el inicio de sesión de Facebook; solucionado

#### Bibliografía de esta semana
Experiencias de futuros y experimentados agricultores y la página de facebook para "developers". 

## VIERNES 24 DE ABRIL DEL 2020
El diseño y funcinalidad de añadir y mostrar los cultivos ya esta implementada; he encontrado dificultades para implementar un RecyclerView en un fragmento; al final ha funcionado aunque hay veces que se queda pillado y no me muestra nada; tengo que estudiarlo.

#### Bibliografía de esta semana 
Varios vídeos de [Youtube](https://www.youtube.com)

## VIERNES 1 DE MAYO DEL 2020
Despues de estar toda la semana estudiando el adapter y cambiar cosas para intentar si funciona; no funciona. Durante la semana que viene seguire con este error. Esta semana tambien he tocado un poco la parte del perfil, sobre todo diseño

#### Bibliografía de esta semana
Ninguna en especial, simplemente he buscado algunos vídeos en [Youtube](https://www.youtube.com) para ver en que me podía estar equivocando; pero no he sacado nada revelante.

<hr>

# VERSIÓN PRELIMINAR (CHECK POINT)

En este punto subo todo el código generado hasta ahora, un enlace a un vídeo explicativo donde digo que hay hecho y que falta y la .apk para que se pueda probar:

📹 [Vídeo explicativo](https://youtu.be/oPF7jsJAuIk) <br>
📳 [APK](https://github.com/ivanperezmolina/ProCampo/blob/master/app-release.apk)


## VIERNES 15 DE MAYO DEL 2020
Solucionado el problema del Recycler View; he mejorado el diseño de este haciendolo mas sencillo y además estoy implementando el menú contextual de toque largo

#### Bibliografía de esta semana
Gracias a la ayuda de algunos profesores y de mi proyecto de la entrega anterior

## VIERNES 22 DE MAYO DEL 2020
La parte de cultivos está totalmente terminada, queda simplemente ver como puedo actualizar el fragments cuando edito o elimino un cultivo. Paso a implementar la parte de recolectas, que depende de los cultivos

#### Bibliografía
Varios vídeos de YouTube y poco más

## SEMANAS DEL 30 DE MAYO Y 5 DE JUNIO  
Durante esta toda la parte de control ya esta finalizada y la parte de venta queda darle unos repasos; por lo general quedan arreglar problemas de compatibilidad, de actualizar fragments y poco más.

#### Bibliografía
Varios vídeos de YouTube y documentación de Firebase, para añadir fotos en storage y demás
