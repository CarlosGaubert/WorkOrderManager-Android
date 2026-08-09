# WorkOrder Manager 📱🛠️

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Language-Java-ED8B00?logo=openjdk&logoColor=white)](https://www.java.com/)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-34%20(Android%2014)-blue)](https://developer.android.com/about/versions/14)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-21%20(Android%205.0)-informational)](https://developer.android.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**WorkOrder Manager** (anteriormente *OTE*) es una aplicación móvil nativa para Android desarrollada en **Java**, diseñada para la gestión, asignación y seguimiento en tiempo real del ciclo de vida de órdenes de trabajo y servicios técnicos.

---

## 🚀 Características y Roles de Usuario

El sistema cuenta con autenticación por **RUT y contraseña** y distribuye el flujo según tres roles principales:

### 1. 🛠️ Administrador / Organizador
- **Gestión de Personal:** Creación, edición y eliminación de personal técnico y administradores.
- **Gestión de Clientes:** Registro y administración de empresas o clientes solicitantes.
- **Gestión de Áreas y Sitios:** Configuración de sedes, plantas o ubicaciones físicas de trabajo.
- **Gestión de Órdenes:** Asignación de órdenes de trabajo entrantes a técnicos específicos con filtros y búsqueda en tiempo real.

### 2. 👷‍♂️ Personal Técnico
- **Recepción de Asignaciones:** Visualización de órdenes de trabajo asignadas en terreno.
- **Aceptación y Seguimiento:** Aceptar órdenes pendientes y cambiar su estado a "En Proceso".
- **Finalización de Órdenes:** Marcar órdenes como completadas al terminar la labor técnica.

### 3. 🏢 Cliente / Solicitante
- **Solicitud de Órdenes:** Creación inmediata de nuevas órdenes de trabajo especificando descripción y fecha.
- **Historial de Solicitudes:** Seguimiento en vivo del estado de sus órdenes de trabajo solicitadas (Sin Aceptar, En Proceso, Terminada).

---

## 🛠️ Stack Tecnológico

| Componente | Tecnología | Detalle |
| :--- | :--- | :--- |
| **Lenguaje** | Java (Java 8 / 11 / 17 / 21) | Código fuente nativo |
| **Plataforma** | Android SDK | `minSdk: 21` (Lollipop), `compileSdk: 34`, `targetSdk: 34` (Android 14) |
| **UI & Layouts** | AndroidX & Material Design 3 | Componentes modernos (`MaterialToolbar`, `TabLayout`, `RecyclerView`, `CardView`) |
| **View Binding** | Android ViewBinding | Enlace seguro y type-safe de vistas |
| **Consumo REST** | Retrofit 2.9.0 + Gson Converter | Comunicación asíncrona con el backend |
| **Notificaciones Push** | Firebase Cloud Messaging (FCM 23.4+) | Soporte con `NotificationChannel` para Android 8.0+ |

---

## 📋 Requisitos del Entorno

Para compilar y ejecutar este proyecto se requiere:
- **Android Studio:** Hedgehog (2023.1.1), Iguana, Jellyfish, Koala, Ladybug o versiones más recientes.
- **JDK (Java Development Kit):** JDK 17 o JDK 21 (incluido por defecto como *Embedded JDK / JBR* en Android Studio).
- **Android SDK:** Android SDK Platform API 34 instalado a través del SDK Manager.
- **Emulador o Dispositivo Físico:** Dispositivo con Android 5.0 (API 21) o superior.

---

## ⚙️ Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone https://github.com/CarlosGaubert/WorkOrderManager-Android.git
cd WorkOrderManager-Android
```

### 2. Abrir en Android Studio
1. Inicia **Android Studio**.
2. Selecciona **Open** (o `File` $\rightarrow$ `Open`) y elige la carpeta raíz del proyecto.
3. Espera a que Android Studio complete la sincronización de Gradle (**Gradle Sync**).

### 3. Ejecutar en Emulador o Dispositivo
1. Asegúrate de tener seleccionado el módulo **`app`** en la barra superior.
2. Selecciona un dispositivo virtual (AVD) o conecta tu teléfono Android vía depuración USB.
3. Presiona el botón verde **Run (Play)** (`Shift + F10` en Windows/Linux o `Control + R` en macOS).

---

## 📁 Estructura del Proyecto

```
WorkOrderManager-Android/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/kendito/ote/
│   │       │   ├── loginActivity.java          # Autenticación y redirección de roles
│   │       │   ├── Common.java                 # Estado y sesión global
│   │       │   ├── api/                        # Interfaces Retrofit y endpoints REST
│   │       │   ├── model/                      # Modelos POJO (Area, Cliente, Personal, etc.)
│   │       │   ├── adapter/                    # Adapters para RecyclerViews
│   │       │   ├── Organizador/                # Vistas y lógica del Administrador
│   │       │   ├── Personal_Organizador/       # Vistas y lógica de Técnicos
│   │       │   ├── Cliente/                    # Vistas y lógica de Clientes
│   │       │   ├── informacion/                # Pantallas de detalle de entidades
│   │       │   └── Service/                    # Servicio Firebase Cloud Messaging
│   │       ├── res/                            # Recursos (layouts, drawables, colors, strings)
│   │       └── AndroidManifest.xml             # Manifiesto de la app con permisos y activities
│   └── build.gradle                            # Configuración del módulo de la app
├── gradle/
├── build.gradle                                # Configuración raíz de Gradle
├── settings.gradle                             # Nombre del proyecto y módulos
├── LICENSE                                     # Licencia MIT
└── README.md
```

---

## 🌐 Configuración del Backend y Seguridad de Red

La aplicación se comunica con un backend REST alojado vía scripts PHP.
- A partir de Android 9 (API 28), Android exige HTTPS de forma predeterminada. El proyecto incluye [`network_security_config.xml`](app/src/main/res/xml/network_security_config.xml) para permitir la conexión en entornos de prueba HTTP.
- Para cambiar la URL base del backend, modifica la constante `URL` en los archivos correspondientes o en [`loginActivity.java`](app/src/main/java/com/kendito/ote/loginActivity.java):
  ```java
  public static final String URL = "http://tu-servidor-o-ip/ote/";
  ```

---

## 📄 Licencia

Este proyecto se distribuye bajo la licencia **MIT**. Consulta el archivo [LICENSE](LICENSE) para más detalles.
