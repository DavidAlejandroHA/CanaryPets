# 🐾 CanaryPets

## Trabajo final de DAW - ECommerce de tienda de productos de animales:

Aplicación web de tienda online canaria de productos para mascotas que colabora de manera local con protectoras y asociaciones de animales, desarrollada con Spring Boot, Thymeleaf y MySQL, desplegada con Docker y Nginx como proxy inverso.

---

## 🚀 Funcionalidades principales

- Catálogo de productos con filtros (categoría, subcategoría, tags, búsqueda)
- Sistema de pedidos y gestión de usuarios
- Panel de administración (CRUD productos, pedidos, etc.)
- Paginación avanzada
- Arquitectura basada en Spring Boot + JPA
- Despliegue con Docker + Nginx

---

## 🐳 Requisitos

- Docker
- Docker Compose

---

## ⚙️ Configuración inicial

### 1. Clonar repositorio

```bash
git clone https://github.com/DavidAlejandroHA/CanaryPets.git
cd CanaryPets
```

### 2. Configurar variables de entorno

Crear archivo `.env`:

```
MYSQL_DATABASE=canarypets
MYSQL_USER=usuario
MYSQL_PASSWORD=password
MYSQL_ROOT_PASSWORD=rootpassword

SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/canarypets_database?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8
SPRING_DATASOURCE_USERNAME=usuario
SPRING_DATASOURCE_PASSWORD=password
```

---

### 3. 🚀 Levantar la aplicación (modo desarrollo por defecto)

```bash
docker compose up -d --build
```

---

### 4. 🌐 Acceder a la aplicación

Modo desarrollo (por defecto):  

```bash
http://localhost/
```

Modo HTTP/HTTPS (tras añadir configuración https + configuración de dominio local):

- HTTP → redirige automáticamente a HTTPS

- HTTPS → [https://www.canarypets.com](https://www.canarypets.com)

---

## 5. 🔐 (Opcional) Configuración HTTPS local (Linux / Ubuntu)

Este proyecto permite habilitar HTTPS en entorno local mediante certificado autofirmado.

⚠️ Nota: el navegador mostrará una advertencia de seguridad al acceder.

### 5.1. Crear carpeta para certificados

Entra a la raíz del proyecto (cd) y ejecuta:

```bash
mkdir certs
```

---  

### 5.2. Generar certificado autofirmado

Ejecuta el siguiente comando:

```bash
openssl req -x509 -nodes -days 365 \
-newkey rsa:2048 \
-keyout certs/privkey.pem \
-out certs/fullchain.pem \
-subj "/CN=www.canarypets.com"
```

---  

### 5.3. Configurar Nginx

Sustituye el archivo `nginx.conf` en la raíz del proyecto por la versión HTTPS (`nginx.conf_https`).  

---  

### 5.4. Configurar Docker ✅

Verifica que el servicio nginx tenga:  

- El siguiente volumen:  
  
  ```bash
  ./certs:/etc/nginx/certs
  ```

- Y exponga los puertos:  
  
  ```bash
  80:80 
  443:443  
  ```

---

### 5.5. 💡 Levantar/Reiniciar aplicación

```bash
docker compose up -d --build
```

---  

### 5.6. Acceso con HTTPS

```bash
https://www.canarypets.com
```

> ⚠️ En caso de haber generado los certificados y entrar con https, el navegador mostrará una advertencia → selecciona "Avanzado" → "Continuar de todos modos".

Si no puedes acceder, recuerda antes asignar como servidor DNS la ip del servidor en el que se encuentre la aplicación, o mirar otras posibles causas del problema en [TROUBLESHOOTING.md](./TROUBLESHOOTING.md)

---

## 6. 🌐 (Opcional) Configuración de dominio local (Bind9)

Si quieres usar un dominio personalizado (`www.canarypets.com`) en red local:  

### ¿Qué hace esta configuración?

- Define una zona DNS (`canarypets.com`)  
- Asocia el dominio a la IP del servidor  
- Permite resolver el dominio dentro de la red local sin depender de Internet  

### Archivos necesarios

- `named.conf.local` → define la zona DNS  
- `db.canarypets.com` → contiene los registros (A, NS)  

Ambos archivos deberán de añadirse en la ruta `/etc/bind`.

Ejemplo:  

- [named.conf.local](./bind_examples/named.conf.local)  
- [db.canarypets.com](./bind_examples/db.canarypets.com)  

⚠️ Importante:  

- Sustituir la IP por la del servidor  
- Configurar tu equipo para usar ese DNS
- Después de añadir los archivos y modificarlos, para reiniciar el servicio `bind9` ejecutar:

```bash
systemctl restart bind9
```

---  

#### 🔐 Firewall (solo producción / servidor)

Si despliegas la aplicación en un servidor Linux (ej: Ubuntu), es recomendable permitir tráfico HTTP/HTTPS:

```bash
sudo ufw allow 80
sudo ufw allow 443
sudo ufw allow 53/tcp
sudo ufw allow 53/udp
sudo ufw enable
```

Y para permitir ssh a conexiones con el servidor:

```bash
sudo ufw allow 22
```

No es necesario en entorno local (localhost).

---

## 🧪 Desarrollo local (resumen rápido)

Si no quieres:

1. Configurar DNS  
2. Configurar HTTPS/HTTP  

Entonces ejecuta:

```bash
docker compose up --build
```

Y ya puedes acceder a la web directamente ejecutando el modo desarrollo por defecto:

```bash
http://localhost/
```

---

### 🧠 Notas

- HTTPS es opcional en entorno local
- De utilizarse HTTPS, el certificado es válido solo en entorno local
- El certificado autofirmado solo es para pruebas
- No se utiliza Let's Encrypt ni Certbot

---

## 📂 Estructura de volúmenes

- `./logs` → logs del backend

- `./nginx/logs` → logs de Nginx

- `./images` → imágenes subidas

---

## 🔐 Seguridad aplicada

### 🌐 Infraestructura (Docker + Nginx)

- ❌ Backend no expuesto públicamente

- ❌ Base de datos no expuesta

- ✅ Solo Nginx accesible (puertos 80/443)

- ✅ Cabeceras proxy correctas (X-Forwarded-For, X-Forwarded-Proto)

- ✅ HTTPS (opcional en local / recomendado en producción)

- ✅ Proxy inverso

### 🛡️ Aplicación (Spring Boot)

- ✅ Protección CSRF en formularios (Spring Security)

- ✅ Validación de inputs (backend)

- ✅ Control de errores y validaciones (slug, precio, etc.)

### 🖥️ Sistema (Servidor)

- 🔒 Firewall (UFW) opcional en producción

- Permitir solo tráfico HTTP/HTTPS (80/443)

- Bloquear el resto de accesos externos

---

## 📌 Consideraciones finales

- Usa `localhost` para desarrollo rápido sin configuraciones adicionales

- HTTPS es opcional en entorno local (solo para pruebas)

- Si algo falla, revisa logs con: `docker compose logs -f`

- Asegúrate de reconstruir contenedores tras cambios importantes:
  
  `docker compose up -d --build`

---

## 🛠️ Troubleshooting

Para ver algunos de los problemas más comunes a la hora de levantar la aplicación junto a las posibles soluciones, visita el documento [TROUBLESHOOTING.md](./TROUBLESHOOTING.md)

## 👨‍💻 Autor

David Alejandro Hernández Alonso - 2º DAW A
