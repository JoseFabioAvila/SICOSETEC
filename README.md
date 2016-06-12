# SICOSETECRepository
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


RECOMENDACIONES
-------------------
Se recomienda usar versión de Andriod mínima de 4.2.2 (Jelly Bean).

INSTALACIÓN
------------
 Se instala como normalmente se haría con un app que no se encuentra en la tienda de google play, mediante el APK directamente, el cual se encargará de instalarla en el dispositivo.

DERECHOS DE AUTOR
-----------------

	"The MIT License (MIT)

	Copyright (c) 2011-2016 SICOSETEC, Inc.

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE."



COLABORADORES
-------------

Colaboradores Actuales:

 * Jose Fabio Ávila Estrada         - fabioavila-16@hotmail.com
 * Jose Pablo Gamboa Vargas         - gamboavargasjosepablo@gmail.com
 * Keylor Gerardo Rojas Araya 	    - morchak@gmail.com
 * Jimmy Alfonso Valverde Alfaro    - xjimmyx45@gmail.com
 * Daniel Antonio Valverde Hidalgo  - dzniel13@hotmail.com 





