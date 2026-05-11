$TTL 604800
@   IN  SOA ns.canarypets.com. admin.canarypets.com. (
        2026050901 ; Serial
        604800     ; Refresh
        86400      ; Retry
        2419200    ; Expire
        604800 )   ; Negative Cache TTL

; Servidor DNS
@       IN  NS      ns.canarypets.com.

; IP del servidor (CAMBIA ESTO)
@       IN  A       192.168.1.100
www     IN  A       192.168.1.100
ns      IN  A       192.168.1.100