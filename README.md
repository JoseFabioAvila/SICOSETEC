# SecsysRepository
Este proyecto es una una herramienta de apoyo para el sector administrativo 
de las empresas que cuentan con un departamento de oficiales de seguridad. 

Facilitando llevar el control de la labor de los oficiales de seguridad,
documentado de forma digital las rondas realizadas y las anomalias encontras 
durante el proceso que dichos guardas realizan, por ejemplo daños en las 
instaciones, divisar personas o actividades sospechosas, que amenacen la 
integridad de las personas en general y de las intalaciones, etc.

La aplicacion se desarrollo en Android nativo, utilizando mapas de Google,
tecnologia NFC, GPS y Itextpdf (version 5.5.9) para la impresion de rondas
en formato PDF.

A continuacion se amplian aspectos tecnicos acerca de la estructuracion
de la aplicacion:

- Activitys: 
  Aqui se encuentran los metodos necesarios para llevar a cabo las acciones 
  de los diferente Layouts de la aplicion, a continuacion se enumeran los activititys.
  - LoginActivity
  - RegistroActivity
  - MainActivity
  - CrearRondaActivity
  - AgregarRutaActivity
  - RegistroActivity
  
- NavigationsOptions: 
  Contiene los fragments que se presentan en el NAvigation Drawer MainActivity.
  - AdministracionDeRondas
  - CrearRutasFragment
  - DescargarReporteFragment
  - RealizarRutasFragment

- Utilidades: 
  Contiene utlidades de importancia para llevar a cabo procesos con el hardware
  del dispositivo, base de datos y creacion de PDF.
  
  - SQLite_Controller: 
    Contiene Las tablas y procedimientos para el manejo de la base de datos.
    
  - PDF_Controller:
    Permite crear los archivos PDF.
    
  - NFC_Controller:
    Permite la escritura y lectura de los datos de los tags NFC.
    
  - GPS_Tracker
    Permite la comunicacion, obtencion de datos de la pocision con el 
    hardware de GPS.
    
- Clases:
  Contiene clases importantes para llevar el control de los datos almacenados 
  en la base de datos. A continuacion se enumeran las clases:
  
  1. Ronda
  2. Ruta
  3. Tag
  4. Usuario

- Adapters:
  Contiene los adaptadores que se utilizan para el despliegue del menu de 
  navegacion.
  
- Popup:
  Contiene popups que se utlizan para guardar ronda en la base de datos 
  y para seleccionar la ruta a seguir por el guarda.

Como se puede volver a ejecutar y si el código debe respetar derechos de autor.






