# GadelApp 📱

Aplicación Android nativa en Java, actualmente en desarrollo inicial. Implementa un flujo de autenticación básico con pantalla de precarga, login, registro y dashboard.

---

## 🚀 Estado del Proyecto

> **En desarrollo** — versión `1.0` (versionCode 1)

---

## 📋 Pantallas / Activities

| Activity | Descripción | Estado |
|---|---|---|
| `PrecargaActivity` | Splash screen con animación (Lottie). Redirige a Login tras 3 segundos. | ✅ Funcional |
| `MainActivity` | Pantalla de Login. Valida email y contraseña (usuario hardcodeado). | ✅ Funcional |
| `RegistroUActivity` | Pantalla de Registro de nuevo usuario. Botón registrar lleva al Dashboard. | 🔧 En progreso (sin lógica real) |
| `DashboardActivity` | Pantalla principal post-login. | 🔧 En progreso (vacía) |

---

## 🔐 Credenciales de Prueba (hardcodeadas)

```
Email:    admin@gmail.com
Password: 12345678
```

> ⚠️ Las credenciales están hardcodeadas en `MainActivity.java`. Pendiente integrar autenticación real.

---

## 🛠️ Tecnologías y Dependencias

- **Lenguaje:** Java
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 36
- **Compile SDK:** 36
- **Build Tools / AGP:** 8.12.3

### Librerías

| Librería | Versión |
|---|---|
| AndroidX AppCompat | 1.7.1 |
| Material Design | 1.13.0 |
| AndroidX Activity | 1.12.4 |
| ConstraintLayout | 2.2.1 |
| Lottie (animaciones) | 5.2.0 |

---

## 📁 Estructura del Proyecto

```
gadelApp/
├── app/
│   └── src/main/java/com/gadel/myapplication/
│       ├── PrecargaActivity.java   # Splash screen
│       ├── MainActivity.java       # Login
│       ├── RegistroUActivity.java  # Registro
│       └── DashboardActivity.java  # Dashboard
├── gradle/
│   └── libs.versions.toml
└── build.gradle
```

---

## 🗺️ Flujo de Navegación

```
PrecargaActivity (3s)
        ↓
MainActivity (Login)
    ↓           ↓
Dashboard   RegistroUActivity
                ↓
           DashboardActivity
```

---

## ⚠️ Pendientes / TODO

- [ ] Implementar autenticación real (Firebase / API REST)
- [ ] Completar lógica del formulario de Registro
- [ ] Desarrollar contenido del Dashboard
- [ ] Manejo de sesión persistente (SharedPreferences / Token)
- [ ] Limitar intentos de login fallidos
- [ ] Agregar navegación hacia Login tras registro exitoso

---

## ▶️ Cómo ejecutar

1. Clonar el repositorio
2. Abrir con **Android Studio**
3. Sincronizar Gradle
4. Ejecutar en emulador o dispositivo físico (Android 7.0+)

