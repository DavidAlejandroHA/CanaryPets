## 🛠️ Troubleshooting

En caso de que tengas problemas a la hora de levantar la aplicación en un servidor Linux / Ubuntu o con la conexión desde el servidor hacia tu PC, estos son algunos de los problemas (y soluciones) más típicos que pueden llegar a ocurrir:

### ❌ No se puede acceder a la aplicación desde el navegador

- Verifica que los contenedores están activos: `docker compose ps`
- Comprueba logs: `docker compose logs -f`
- Asegúrate de que los puertos 80 y 443 están libres: `sudo lsof -i :80`
- Si hay conflictos (ej: Apache): `sudo systemctl stop apache2`

---

### ❌ Error: "address already in use"

- Otro servicio está usando el puerto 80/443 (probablemente apache o nginx ya instalado en la propia máquina)
- Solución: `sudo systemctl stop apache2` `sudo systemctl disable apache2` -> parar y deshabilitar apache
  
  o
- `sudo systemctl stop nginx` `sudo systemctl disable nginx` -> parar y deshabilitar nginx

---

### ❌ Nginx se reinicia continuamente

- Revisar error típico: `host not found in upstream "backend"`
- Asegurarse de que:
  - El servicio backend está levantado
  - Existe en `docker-compose.yml`
  - Nginx depende del backend (`depends_on`)

---

### ❌ DNS no funciona (bind9)

- Asegurarse de abrir correctamente el puerto 53: `sudo ufw allow 53/tcp` `sudo ufw allow 53/udp`
- Verificar estado: `sudo systemctl status bind9`
- Probar resolución: `nslookup www.canarypets.com 192.168.1.123`

---

### ❌ "DNS_PROBE_POSSIBLE" o dominio no resuelve

- Verificar que el cliente usa el DNS correcto
- Limpiar caché DNS:
  - Windows: `ipconfig /flushdns`
  - Linux: `sudo systemd-resolve --flush-caches`

---

### ❌ Problemas con apt (instalaciones fallan)

- Limpiar y actualizar repositorios: 
  - `sudo apt clean` 
  - `sudo rm -rf /var/lib/apt/lists/*`
  -  `sudo apt update`

---

### ❌ No funciona SSH

- Asegurar puerto abierto: `sudo ufw allow 22`
- Verificar estado: `sudo systemctl status ssh`

---

### ℹ️ Nota sobre PTR (resolución inversa)

- No es necesario para el funcionamiento básico
- Solo afecta a que `nslookup` muestre "Unknown"

---

## 🧪 Acceso sin DNS (archivo hosts)

Si no se desea configurar un servidor DNS, se puede usar el archivo `hosts` para resolver el dominio manualmente.

### 🪟 Windows

Editar como administrador: `C:\Windows\System32\drivers\etc\hosts`

Añadir:

```
192.168.1.123 canarypets.com
192.168.1.123 www.canarypets.com
```

Luego ejecutar: `ipconfig /flushdns`

---

### 🐧 Linux / Ubuntu

Editar: `/etc/hosts`

Añadir:

```
192.168.1.123 canarypets.com
192.168.1.123 www.canarypets.com
```

---

### ⚠️ Notas

- Método recomendado para desarrollo rápido
- No requiere bind9 ni configuración de red adicional
- Prioriza sobre DNS (puede causar confusión si ambos están activos)
