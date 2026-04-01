package com.canarypets.backend.seeders;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.Tag;
import com.canarypets.backend.repositories.CategoryRepository;
import com.canarypets.backend.repositories.ProductRepository;
import com.canarypets.backend.repositories.TagRepository;
import com.canarypets.backend.services.ProductService;
import com.canarypets.backend.services.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(3)
@RequiredArgsConstructor
public class ProductsSeeder implements CommandLineRunner {
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final ProductService productService;
    @Autowired
    private final TagService tagService;

    @Override
    @Transactional
    public void run(String... args) {
        if (productRepository.count() == 0) {
            seedProducts();
        }
    }

    private void seedProducts() {
        //Category comidaPerros = categoryRepository.findBySlug("comida-perros").orElseThrow();
        seedPerros();
        seedGatos();
        seedRoedores();
        seedPeces();
        seedPajaros();
        seedReptiles();

        /*Category comidaPeces = categoryRepository.findBySlugAndParent_Slug("comida", "peces").orElseThrow();

        createProduct("Tetra Comida para peces tropicales", "Alimento equilibrado para peces", new BigDecimal("12.99"), 50, false,
                "10x5x3 cm", "Tetra", "peces/comida1.jpg", comidaPeces, "Tetra", "Escamas", null / Sin edad/);*/
    }

    private void seedPerros() {
        Category comidaPerros = categoryRepository.findBySlugAndParent_Slug("comida", "perros").orElseThrow();
        Category accesoriosPerros = categoryRepository.findBySlugAndParent_Slug("accesorios", "perros").orElseThrow();
        Category juguetesPerros = categoryRepository.findBySlugAndParent_Slug("juguetes", "perros").orElseThrow();
        Category saludPerros = categoryRepository.findBySlugAndParent_Slug("salud", "perros").orElseThrow();
        Category higienePerros = categoryRepository.findBySlugAndParent_Slug("higiene", "perros").orElseThrow();

        createProduct("Royal Canin Pienso cachorro", "Alimento para cachorros", new BigDecimal("29.99"), 50, false, "30x20x10 cm", "Royal Canin", "productos/perros/rc-pienso.jpg", comidaPerros, "Royal Canin", "Pienso", "Cachorro");
        createProduct("Advance Pienso adulto", "Alimento equilibrado", new BigDecimal("27.99"), 40, false, "30x20x10 cm", "Advance", "productos/perros/advance-pienso.jpg", comidaPerros, "Advance", "Pienso", "Adulto");
        createProduct("Purina Pro Plan húmedo", "Comida húmeda", new BigDecimal("19.99"), 30, false, "20x10x5 cm", "Purina Pro Plan", "productos/perros/purina-humedo.jpg", comidaPerros, "Purina Pro Plan", "Húmeda", null);
        createProduct("Acana Snacks naturales", "Snacks saludables", new BigDecimal("14.99"), 20, true, "10x5x5 cm", "Acana", "productos/perros/acana-snacks.jpg", comidaPerros, "Acana", "Snacks", null);

        createProduct("Royal Canin Cama premium", "Cama cómoda", new BigDecimal("49.99"), 10, false, "60x40x20 cm", "Royal Canin", "productos/perros/cama.jpg", accesoriosPerros, "Royal Canin", "Camas", null);
        createProduct("Advance Correa resistente", "Correa duradera", new BigDecimal("15.99"), 25, false, "120x2 cm", "Advance", "productos/perros/correa.jpg", accesoriosPerros, "Advance", "Correas y Arneses", null);

        createProduct("Purina Mordedor goma", "Juguete resistente", new BigDecimal("9.99"), 35, false, "10x5 cm", "Purina Pro Plan", "productos/perros/mordedor.jpg", juguetesPerros, "Purina Pro Plan", "Mordedores", null);
        createProduct("Acana Juguete interactivo", "Estimulación mental", new BigDecimal("12.99"), 1, true, "15x10 cm", "Acana", "productos/perros/interactivo.jpg", juguetesPerros, "Acana", "Interactivos", null);

        createProduct("Royal Canin Pienso senior", "Para perros mayores", new BigDecimal("28.99"), 20, false, "30x20x10 cm", "Royal Canin", "productos/perros/rc-senior.jpg", comidaPerros, "Royal Canin", "Pienso", "Senior");
        createProduct("Advance Snacks dentales", "Cuida dientes", new BigDecimal("13.99"), 35, false, "10x5x5 cm", "Advance", "productos/perros/snacks-dentales.jpg", comidaPerros, "Advance", "Snacks", null);
        createProduct("Purina Cama básica perro", "Cama sencilla", new BigDecimal("34.99"), 12, false, "60x40x20 cm", "Purina", "productos/perros/cama-basica.jpg", accesoriosPerros, "Purina", "Camas", null);
        createProduct("Acana Juguete cuerda", "Juguete resistente", new BigDecimal("11.99"), 1, true, "20x5 cm", "Acana", "productos/perros/cuerda.jpg", juguetesPerros, "Acana", "Mordedores", null);

        createProduct("Acana Pienso natural perro", "Alimento premium", new BigDecimal("31.99"), 25, false, "30x20x10 cm", "Acana", "productos/perros/acana-natural.jpg", comidaPerros, "Acana", "Pienso", null);
        createProduct("Purina Snack entrenamiento", "Ideal para adiestramiento", new BigDecimal("10.99"), 40, false, "10x5x5 cm", "Purina", "productos/perros/snack-training.jpg", comidaPerros, "Purina", "Snacks", null);
        createProduct("Advance Arnés cómodo", "Ajuste ergonómico", new BigDecimal("18.99"), 15, false, "50x5 cm", "Advance", "productos/perros/arnes.jpg", accesoriosPerros, "Advance", "Correas y Arneses", null);
        createProduct("Royal Canin Pelota resistente", "Juguete duradero", new BigDecimal("7.99"), 1, true, "8x8 cm", "Royal Canin", "productos/perros/pelota.jpg", juguetesPerros, "Royal Canin", "Interactivos", null);

        createProduct("Royal Canin Pienso adulto perro", "Alimento completo para perros adultos", new BigDecimal("49.99"), 20, false, "40x30x10 cm", "Royal Canin", "productos/perros/pienso-adulto.jpg", comidaPerros, "Royal Canin", "Pienso", "adulto");
        createProduct("Purina Snack dental perro", "Ayuda a la higiene bucal", new BigDecimal("6.49"), 35, false, "15x5 cm", "Purina", "productos/perros/snack-dental.jpg", comidaPerros, "Purina", "Snacks", null);
        createProduct("Trixie Arnés ajustable", "Arnés cómodo y resistente", new BigDecimal("14.99"), 25, false, "20x10 cm", "Trixie", "productos/perros/arnes-ajustable.jpg", accesoriosPerros, "Trixie", "Correas y Arneses", null);
        createProduct("Bayer Champú antiparasitario", "Protección contra pulgas", new BigDecimal("11.99"), 18, true, "10x5 cm", "Bayer", "productos/perros/champu-antiparasitario.jpg", higienePerros, "Bayer", "Champú", null);

        createProduct("Bayer Antiparasitario oral perro", "Protección interna", new BigDecimal("14.99"), 20, false, "10x5 cm", "Bayer", "productos/perros/antiparasitario-oral.jpg", saludPerros, "Bayer", "Antiparasitarios", null);
        createProduct("Beaphar Vitaminas perro", "Refuerzo inmunológico", new BigDecimal("9.49"), 25, false, "5x5x10 cm", "Beaphar", "productos/perros/vitaminas.jpg", saludPerros, "Beaphar", "Suplementos", null);
        createProduct("Advance Suplemento digestivo", "Mejora digestión", new BigDecimal("11.99"), 18, false, "5x5x10 cm", "Advance", "productos/perros/digestivo.jpg", saludPerros, "Advance", "Tratamientos", null);
        createProduct("Royal Canin Cuidado articular", "Salud de articulaciones", new BigDecimal("19.99"), 15, true, "5x5x10 cm", "Royal Canin", "productos/perros/articular.jpg", saludPerros, "Royal Canin", "Suplementos", null);
        createProduct("Purina Tratamiento piel perro", "Cuidado dermatológico", new BigDecimal("13.49"), 22, false, "5x5x10 cm", "Purina", "productos/perros/piel.jpg", saludPerros, "Purina", "Tratamientos", null);

        createProduct("Trixie Champú piel sensible", "Limpieza suave", new BigDecimal("8.99"), 30, false, "10x5 cm", "Trixie", "productos/perros/champu-sensible.jpg", higienePerros, "Trixie", "Champú", null);
        createProduct("Beaphar Toallitas húmedas perro", "Higiene rápida", new BigDecimal("6.49"), 40, false, "10x5 cm", "Beaphar", "productos/perros/toallitas.jpg", higienePerros, "Beaphar", "Toallitas", null);
        createProduct("Bayer Spray limpieza patas", "Limpieza sin agua", new BigDecimal("7.99"), 28, false, "10x5 cm", "Bayer", "productos/perros/spray-patas.jpg", higienePerros, "Bayer", "Higiene", null);
        createProduct("Trixie Cepillo deslanador", "Elimina pelo muerto", new BigDecimal("12.99"), 18, true, "20x5 cm", "Trixie", "productos/perros/cepillo.jpg", higienePerros, "Trixie", "Cepillos", null);
        createProduct("Advance Champú antipulgas", "Protección externa", new BigDecimal("10.99"), 20, false, "10x5 cm", "Advance", "productos/perros/champu-antipulgas.jpg", higienePerros, "Advance", "Champú", null);

        // Dieta veterinaria
        createProduct("Royal Canin Dieta veterinaria digestiva", "Pienso especial digestivo", new BigDecimal("32.99"), 20, false, "10kg", "Royal Canin", "productos/perros/dieta-veterinaria1.jpg", comidaPerros, "Royal Canin", "Dieta", null);
        // Transportines
        createProduct("Trixie Transportín compacto", "Transportín resistente", new BigDecimal("45.99"), 15, false, "60x40x40 cm", "Trixie", "productos/perros/transportin1.jpg", accesoriosPerros, "Trixie", "Transportines", null);
        // Correas
        createProduct("Flexi Correa extensible roja", "Correa resistente extensible", new BigDecimal("19.99"), 30, false, "5m", "Flexi", "productos/perros/correa1.jpg", accesoriosPerros, "Flexi", "Correas y arneses", null);
        // Comederos
        createProduct("Trixie Comedero acero inoxidable", "Comedero duradero", new BigDecimal("9.99"), 40, false, "20cm", "Trixie", "productos/perros/comedero1.jpg", accesoriosPerros, "Trixie", "Comederos y bebederos", null);
        // Champú
        createProduct("Menforsan Champú hidratante perro", "Champú suave", new BigDecimal("8.99"), 25, false, "500ml", "Menforsan", "productos/perros/champu1.jpg", higienePerros, "Menforsan", "Champú", null);
        // Cepillos
        createProduct("Trixie Cepillo doble para perros", "Cepillo desenredante", new BigDecimal("12.50"), 18, false, "20cm", "Trixie", "productos/perros/cepillo1.jpg", higienePerros, "Trixie", "Cepillos", null);

    }

    private void seedGatos() {
        Category comidaGatos = categoryRepository.findBySlugAndParent_Slug("comida", "gatos").orElseThrow();
        Category accesoriosGatos = categoryRepository.findBySlugAndParent_Slug("accesorios", "gatos").orElseThrow();
        Category juguetesGatos = categoryRepository.findBySlugAndParent_Slug("juguetes", "gatos").orElseThrow();
        Category saludGatos = categoryRepository.findBySlugAndParent_Slug("salud", "gatos").orElseThrow();
        Category higieneGatos = categoryRepository.findBySlugAndParent_Slug("higiene", "gatos").orElseThrow();

        createProduct("Hill’s Pienso gato adulto", "Alimento equilibrado", new BigDecimal("24.99"), 40, false, "30x20x10 cm", "Hill’s", "productos/gatos/hills-pienso.jpg", comidaGatos, "Hill’s", "Pienso", "Adulto");
        createProduct("Royal Canin Pienso gato esterilizado", "Control de peso", new BigDecimal("26.99"), 35, false, "30x20x10 cm", "Royal Canin", "productos/gatos/rc-esterilizado.jpg", comidaGatos, "Royal Canin", "Pienso", "Adulto");
        createProduct("Purina Gourmet húmedo gato", "Comida húmeda premium", new BigDecimal("18.99"), 50, false, "20x10x5 cm", "Purina", "productos/gatos/purina-humedo.jpg", comidaGatos, "Purina", "Húmeda", null);
        createProduct("Advance Snacks gato", "Snacks saludables", new BigDecimal("9.99"), 60, true, "10x5x5 cm", "Advance", "productos/gatos/advance-snacks.jpg", comidaGatos, "Advance", "Snacks", null);

        createProduct("Hill’s Cama gato confort", "Cama acolchada", new BigDecimal("39.99"), 15, false, "50x40x20 cm", "Hill’s", "productos/gatos/cama.jpg", accesoriosGatos, "Hill’s", "Camas", null);
        createProduct("Royal Canin Transportín gato", "Transporte seguro", new BigDecimal("29.99"), 20, false, "45x30x30 cm", "Royal Canin", "productos/gatos/transportin.jpg", accesoriosGatos, "Royal Canin", "Transportines", null);
        createProduct("Purina Rascador básico", "Rascador compacto", new BigDecimal("19.99"), 25, false, "40x20x20 cm", "Purina", "productos/gatos/rascador.jpg", accesoriosGatos, "Purina", "Rascadores", null);

        createProduct("Advance Pelota gato", "Juguete ligero", new BigDecimal("5.99"), 80, false, "5x5 cm", "Advance", "productos/gatos/pelota.jpg", juguetesGatos, "Advance", "Pelotas", null);
        createProduct("Hill’s Juguete pluma", "Estimula el juego", new BigDecimal("6.99"), 70, false, "20x5 cm", "Hill’s", "productos/gatos/pluma.jpg", juguetesGatos, "Hill’s", "Interactivos", null);
        createProduct("Royal Canin Ratón juguete", "Juguete clásico", new BigDecimal("4.99"), 1, true, "10x5 cm", "Royal Canin", "productos/gatos/raton.jpg", juguetesGatos, "Royal Canin", "Ratones", null);
        createProduct("Hill’s Juguete Ratón", "Juguete para gatos", new BigDecimal("6.50"), 70, false, "20x5 cm", "Hill’s", "productos/gatos/hills-raton.jpg", juguetesGatos, "Hill’s", "Ratones", null);

        createProduct("Purina Tratamiento antipulgas", "Protección básica", new BigDecimal("14.99"), 30, false, "5x5x10 cm", "Purina", "productos/gatos/antipulgas.jpg", saludGatos, "Purina", "Tratamientos", null);
        createProduct("Advance Vitaminas gato", "Refuerzo salud", new BigDecimal("12.99"), 25, false, "5x5x10 cm", "Advance", "productos/gatos/vitaminas.jpg", saludGatos, "Advance", "Suplementos", null);
        createProduct("Hill’s Pasta digestiva", "Mejora digestión", new BigDecimal("11.99"), 20, false, "10x3 cm", "Hill’s", "productos/gatos/digestiva.jpg", saludGatos, "Hill’s", "Suplementos", null);
        createProduct("Royal Canin Tratamiento completo", "Cuidado integral", new BigDecimal("16.99"), 10, true, "5x5x10 cm", "Royal Canin", "productos/gatos/tratamiento.jpg", saludGatos, "Royal Canin", "Tratamientos", null);
        createProduct("Purina Arena higiénica", "Arena absorbente", new BigDecimal("13.99"), 45, false, "30x20x10 cm", "Purina", "productos/gatos/arena.jpg", higieneGatos, "Purina", "Higiene", null);

        createProduct("Advance Pienso gato senior", "Para gatos mayores", new BigDecimal("23.99"), 20, false, "30x20x10 cm", "Advance", "productos/gatos/senior.jpg", comidaGatos, "Advance", "Pienso", "Senior");
        createProduct("Hill’s Snack dental gato", "Cuidado bucal", new BigDecimal("8.99"), 35, false, "10x5x5 cm", "Hill’s", "productos/gatos/dental.jpg", comidaGatos, "Hill’s", "Snacks", null);
        //createProduct("Royal Canin Fuente agua gato", "Agua en movimiento", new BigDecimal("34.99"), 12, false, "20x20 cm", "Royal Canin", "productos/gatos/fuente.jpg", accesoriosGatos, "Royal Canin", "Accesorios", null);
        createProduct("Purina Juguete láser gato", "Juego interactivo", new BigDecimal("9.99"), 1, true, "10x5 cm", "Purina", "productos/gatos/laser.jpg", juguetesGatos, "Purina", "Interactivos", null);

        createProduct("Royal Canin Pienso gato esterilizado", "Control de peso", new BigDecimal("42.99"), 22, false, "40x30x10 cm", "Royal Canin", "productos/gatos/pienso-esterilizado.jpg", comidaGatos, "Royal Canin", "Pienso", "adulto");
        createProduct("Whiskas Comida húmeda pollo", "Alta palatabilidad", new BigDecimal("5.49"), 40, false, "10x5 cm", "Whiskas", "productos/gatos/comida-humeda.jpg", comidaGatos, "Whiskas", "Húmeda", null);
        createProduct("Trixie Rascador compacto", "Ideal para espacios pequeños", new BigDecimal("19.99"), 15, false, "30x30x50 cm", "Trixie", "productos/gatos/rascador-compacto.jpg", accesoriosGatos, "Trixie", "Rascadores", null);
        createProduct("Beaphar Spray antipulgas gato", "Protección eficaz", new BigDecimal("9.99"), 20, true, "10x5 cm", "Beaphar", "productos/gatos/spray-antipulgas.jpg", saludGatos, "Beaphar", "Antiparasitarios", null);

        createProduct("Trixie Arena aglomerante gato", "Control de olores", new BigDecimal("9.99"), 35, false, "30x20x10 cm", "Trixie", "productos/gatos/arena.jpg", higieneGatos, "Trixie", "Arena", null);
        createProduct("Vitakraft Arena vegetal gato", "Ecológica", new BigDecimal("11.49"), 28, false, "30x20x10 cm", "Vitakraft", "productos/gatos/arena-vegetal.jpg", higieneGatos, "Vitakraft", "Arena", null);
        createProduct("Beaphar Toallitas gato", "Limpieza rápida", new BigDecimal("5.99"), 40, false, "10x5 cm", "Beaphar", "productos/gatos/toallitas.jpg", higieneGatos, "Beaphar", "Higiene", null);
        createProduct("Trixie Champú seco gato", "Sin agua", new BigDecimal("7.49"), 25, false, "10x5 cm", "Trixie", "productos/gatos/champu-seco.jpg", higieneGatos, "Trixie", "Champu", null);
        createProduct("Bayer Spray higiene gato", "Cuidado diario", new BigDecimal("8.49"), 20, true, "10x5 cm", "Bayer", "productos/gatos/spray.jpg", higieneGatos, "Bayer", "Higiene", null);
        createProduct("Vitakraft Desodorante arenero", "Neutraliza olores", new BigDecimal("6.99"), 30, false, "10x5 cm", "Vitakraft", "productos/gatos/desodorante.jpg", higieneGatos, "Vitakraft", "Higiene", null);

        // Camas
        createProduct("Trixie Cama acolchada gato", "Cama suave", new BigDecimal("29.99"), 15, false, "50cm", "Trixie", "productos/gatos/cama1.jpg", accesoriosGatos, "Trixie", "Camas", null);

        // Transportines
        createProduct("Catit Transportín gris", "Transportín cómodo", new BigDecimal("39.99"), 12, false, "55cm", "Catit", "productos/gatos/transportin1.jpg", accesoriosGatos, "Catit", "Transportines", null);

        // Areneros
        createProduct("Arquivet Arenero cerrado", "Arenero con tapa", new BigDecimal("34.99"), 20, false, "60cm", "Arquivet", "productos/gatos/arenero1.jpg", accesoriosGatos, "Arquivet", "Areneros", null);

        // Comederos
        createProduct("Trixie Comedero doble gato", "Comedero doble", new BigDecimal("11.99"), 25, false, "30cm", "Trixie", "productos/gatos/comedero1.jpg", accesoriosGatos, "Trixie", "Comederos y bebederos", null);

        // Pelotas
        createProduct("Catit Pelota interactiva", "Juguete divertido", new BigDecimal("6.99"), 30, false, "5cm", "Catit", "productos/gatos/pelota1.jpg", juguetesGatos, "Catit", "Pelotas", null);

        // Cepillos (2)
        createProduct("Trixie Cepillo suave gato", "Cepillo delicado", new BigDecimal("9.99"), 20, false, "15cm", "Trixie", "productos/gatos/cepillo1.jpg", higieneGatos, "Trixie", "Cepillos", null);
        createProduct("Arquivet Cepillo deslanador gato", "Elimina pelo muerto", new BigDecimal("14.99"), 18, false, "18cm", "Arquivet", "productos/gatos/cepillo2.jpg", higieneGatos, "Arquivet", "Cepillos", null);

        // Champú (1)
        createProduct("Menforsan Champú gato suave", "Champú específico gatos", new BigDecimal("7.99"), 22, false, "300ml", "Menforsan", "productos/gatos/champu1.jpg", higieneGatos, "Menforsan", "Champú", null);
    }

    private void seedRoedores() {
        Category comidaRoedores = categoryRepository.findBySlugAndParent_Slug("comida", "roedores").orElseThrow();
        Category accesoriosRoedores = categoryRepository.findBySlugAndParent_Slug("accesorios", "roedores").orElseThrow();
        //Category juguetesRoedores = categoryRepository.findBySlugAndParent_Slug("juguetes", "roedores").orElseThrow();
        //Category saludRoedores = categoryRepository.findBySlugAndParent_Slug("salud", "roedores").orElseThrow();
        Category higieneRoedores = categoryRepository.findBySlugAndParent_Slug("higiene", "roedores").orElseThrow();

// 🔹 COMIDA
        createProduct("Vitakraft Heno natural", "Alimento base para roedores", new BigDecimal("8.99"), 50, false, "20x10x10 cm", "Vitakraft", "productos/roedores/heno.jpg", comidaRoedores, "Vitakraft", "Heno", null);
        createProduct("Versele Laga Pienso completo", "Nutrición equilibrada", new BigDecimal("12.99"), 40, false, "20x10x10 cm", "Versele Laga", "productos/roedores/pienso.jpg", comidaRoedores, "Versele Laga", "Pienso", null);
        createProduct("Bunny Snacks frutas", "Snacks naturales", new BigDecimal("6.99"), 60, true, "10x5x5 cm", "Bunny", "productos/roedores/snacks.jpg", comidaRoedores, "Bunny", "Snacks", null);
        createProduct("Vitakraft Mix semillas", "Mezcla variada", new BigDecimal("9.49"), 30, false, "20x10x10 cm", "Vitakraft", "productos/roedores/semillas.jpg", comidaRoedores, "Vitakraft", "Semillas", null);

// 🔹 ACCESORIOS
        createProduct("Ferplast Jaula básica", "Jaula para roedores", new BigDecimal("49.99"), 10, false, "50x30x30 cm", "Ferplast", "productos/roedores/jaula.jpg", accesoriosRoedores, "Ferplast", "Jaulas", null);
        createProduct("Trixie Bebedero automático", "Bebedero práctico", new BigDecimal("7.99"), 35, false, "10x5 cm", "Trixie", "productos/roedores/bebedero.jpg", accesoriosRoedores, "Trixie", "Comederos y Bebederos", null);
        createProduct("Ferplast Comedero plástico", "Comedero resistente", new BigDecimal("5.99"), 45, false, "10x5 cm", "Ferplast", "productos/roedores/comedero.jpg", accesoriosRoedores, "Ferplast", "Comederos y Bebederos", null);

// 🔹 JUGUETES
//        createProduct("Trixie Rueda ejercicio", "Rueda silenciosa", new BigDecimal("14.99"), 25, false, "20x20 cm", "Trixie", "productos/roedores/rueda.jpg", juguetesRoedores, "Trixie", "Ruedas", null);
//        createProduct("Bunny Túnel madera", "Túnel para juego", new BigDecimal("11.99"), 20, false, "25x10 cm", "Bunny", "productos/roedores/tunel.jpg", juguetesRoedores, "Bunny", "Túneles", null);
//        createProduct("Vitakraft Juguete mordedor", "Madera natural", new BigDecimal("4.99"), 1, true, "10x3 cm", "Vitakraft", "productos/roedores/mordedor.jpg", juguetesRoedores, "Vitakraft", "Mordedores", null);

// 🔹 SALUD
//        createProduct("Beaphar Vitaminas roedores", "Suplemento vitamínico", new BigDecimal("10.99"), 20, false, "5x5x10 cm", "Beaphar", "productos/roedores/vitaminas.jpg", saludRoedores, "Beaphar", "Suplementos", null);
//        createProduct("Vitakraft Tratamiento digestivo", "Mejora digestión", new BigDecimal("9.99"), 15, false, "5x5x10 cm", "Vitakraft", "productos/roedores/digestivo.jpg", saludRoedores, "Vitakraft", "Tratamientos", null);
//        createProduct("Bunny Cuidado dental", "Desgaste dientes", new BigDecimal("8.49"), 18, false, "10x5 cm", "Bunny", "productos/roedores/dental.jpg", saludRoedores, "Bunny", "Suplementos", null);
//        createProduct("Beaphar Tratamiento completo", "Cuidado general", new BigDecimal("12.99"), 12, true, "5x5x10 cm", "Beaphar", "productos/roedores/tratamiento.jpg", saludRoedores, "Beaphar", "Tratamientos", null);

// 🔹 HIGIENE
        createProduct("Vitakraft Lecho absorbente", "Sustrato higiénico", new BigDecimal("7.99"), 50, false, "30x20x10 cm", "Vitakraft", "productos/roedores/lecho.jpg", higieneRoedores, "Vitakraft", "Lechos", null);
        //createProduct("Trixie Arena natural", "Arena ecológica", new BigDecimal("6.99"), 40, false, "30x20x10 cm", "Trixie", "productos/roedores/arena.jpg", higieneRoedores, "Trixie", "Arena", null);
        createProduct("Bunny Higiene premium", "Alta absorción", new BigDecimal("9.99"), 35, false, "30x20x10 cm", "Bunny", "productos/roedores/higiene.jpg", higieneRoedores, "Bunny", "Lechos", null);
        createProduct("Ferplast Lecho ecológico", "Material reciclado", new BigDecimal("8.49"), 1, true, "30x20x10 cm", "Ferplast", "productos/roedores/lecho-eco.jpg", higieneRoedores, "Ferplast", "Lechos", null);

        createProduct("Bunny Pienso junior roedores", "Para roedores jóvenes", new BigDecimal("11.49"), 25, false, "20x10x10 cm", "Bunny", "productos/roedores/junior.jpg", comidaRoedores, "Bunny", "Pienso", "Cachorro");
        createProduct("Vitakraft Barritas miel", "Snack dulce", new BigDecimal("5.99"), 40, false, "10x5 cm", "Vitakraft", "productos/roedores/barritas.jpg", comidaRoedores, "Vitakraft", "Snacks", null);
        createProduct("Trixie Casa madera", "Refugio natural", new BigDecimal("14.99"), 20, false, "20x15 cm", "Trixie", "productos/roedores/casa.jpg", accesoriosRoedores, "Trixie", "Refugios", null);
//        createProduct("Ferplast Juguete colgante", "Entretenimiento", new BigDecimal("6.99"), 1, true, "15x5 cm", "Ferplast", "productos/roedores/colgante.jpg", juguetesRoedores, "Ferplast", "Interactivos", null);

        createProduct("Versele Laga Heno premium", "Heno natural seleccionado", new BigDecimal("9.49"), 30, false, "20x10x10 cm", "Versele Laga", "productos/roedores/heno-premium.jpg", comidaRoedores, "Versele Laga", "Heno", null);
        createProduct("Bunny Mix natural roedores", "Mezcla equilibrada", new BigDecimal("10.99"), 25, false, "20x10x10 cm", "Bunny", "productos/roedores/mix-natural.jpg", comidaRoedores, "Bunny", "Semillas", null);
        createProduct("Trixie Bebedero cristal", "Bebedero resistente", new BigDecimal("8.49"), 35, false, "10x5 cm", "Trixie", "productos/roedores/bebedero-cristal.jpg", accesoriosRoedores, "Trixie", "Comederos y Bebederos", null);
        createProduct("Vitakraft Lecho vegetal", "Sustrato ecológico", new BigDecimal("7.49"), 40, true, "30x20x10 cm", "Vitakraft", "productos/roedores/lecho-vegetal.jpg", higieneRoedores, "Vitakraft", "Lechos", null);

        createProduct("Bunny Heno montaña", "Heno fresco de alta calidad", new BigDecimal("10.49"), 28, false, "20x10x10 cm", "Bunny", "productos/roedores/heno-montana.jpg", comidaRoedores, "Bunny", "Heno", null);
        createProduct("Vitakraft Mix verduras", "Alimento con vegetales", new BigDecimal("9.99"), 35, false, "20x10x10 cm", "Vitakraft", "productos/roedores/mix-verduras.jpg", comidaRoedores, "Vitakraft", "Semillas", null);
        createProduct("Ferplast Comedero cerámica", "Base antideslizante", new BigDecimal("6.99"), 30, false, "10x5 cm", "Ferplast", "productos/roedores/comedero-ceramica.jpg", accesoriosRoedores, "Ferplast", "Comederos y Bebederos", null);
        createProduct("Trixie Sustrato papel reciclado", "Alta absorción", new BigDecimal("8.49"), 40, true, "30x20x10 cm", "Trixie", "productos/roedores/sustrato-papel.jpg", higieneRoedores, "Trixie", "Lechos", null);

        // Jaulas
        createProduct("Ferplast Jaula roedores básica", "Jaula espaciosa", new BigDecimal("49.99"), 10, false, "80cm", "Ferplast", "productos/roedores/jaula1.jpg", accesoriosRoedores, "Ferplast", "Jaulas", null);

        // Refugio (1)
        createProduct("Trixie Refugio madera roedor", "Refugio natural", new BigDecimal("14.99"), 15, false, "20cm", "Trixie", "productos/roedores/refugio1.jpg", accesoriosRoedores, "Trixie", "Refugios", null);

        // Limpieza (2)
        createProduct("Vitakraft Arena higiene roedores", "Lecho absorbente", new BigDecimal("6.99"), 40, false, "2kg", "Vitakraft", "productos/roedores/limpieza1.jpg", higieneRoedores, "Vitakraft", "Limpieza", null);
        createProduct("Arquivet Lecho vegetal roedores", "Lecho ecológico", new BigDecimal("8.99"), 35, false, "3kg", "Arquivet", "productos/roedores/limpieza2.jpg", higieneRoedores, "Arquivet", "Limpieza", null);
    }

    private void seedPeces() {
        Category comidaPeces = categoryRepository.findBySlugAndParent_Slug("comida", "peces").orElseThrow();
        Category accesoriosPeces = categoryRepository.findBySlugAndParent_Slug("accesorios", "peces").orElseThrow();
        Category saludPeces = categoryRepository.findBySlugAndParent_Slug("salud", "peces").orElseThrow();

        createProduct("Tetra Escamas tropicales", "Alimento equilibrado para peces", new BigDecimal("12.99"), 50, false, "10x5x3 cm", "Tetra", "productos/peces/tetra-escamas.jpg", comidaPeces, "Tetra", "Escamas", null);
        createProduct("JBL Granulado premium peces", "Granulado nutritivo", new BigDecimal("14.50"), 40, false, "10x5x3 cm", "JBL", "productos/peces/jbl-granulado.jpg", comidaPeces, "JBL", "Granulado", null);
        createProduct("Sera Escamas básicas", "Comida diaria para peces", new BigDecimal("9.99"), 30, false, "10x5x3 cm", "Sera", "productos/peces/sera-escamas.jpg", comidaPeces, "Sera", "Escamas", null);
        createProduct("Fluval Granulado avanzado", "Nutrición completa", new BigDecimal("16.99"), 20, true, "10x5x3 cm", "Fluval", "productos/peces/fluval-granulado.jpg", comidaPeces, "Fluval", "Granulado", null);

        createProduct("Tetra Acuario 40L", "Acuario compacto", new BigDecimal("79.99"), 10, false, "40x30x30 cm", "Tetra", "productos/peces/tetra-acuario.jpg", accesoriosPeces, "Tetra", "Acuarios", null);
        createProduct("JBL Filtro interno", "Filtro eficiente", new BigDecimal("29.99"), 25, false, "15x10x5 cm", "JBL", "productos/peces/jbl-filtro.jpg", accesoriosPeces, "JBL", "Filtros", null);
        createProduct("Sera Iluminación LED", "Luz para acuarios", new BigDecimal("34.99"), 15, false, "30x5x3 cm", "Sera", "productos/peces/sera-luz.jpg", accesoriosPeces, "Sera", "Iluminación", null);
        createProduct("Fluval Filtro externo", "Filtro potente", new BigDecimal("89.99"), 5, true, "25x20x20 cm", "Fluval", "productos/peces/fluval-filtro.jpg", accesoriosPeces, "Fluval", "Filtros", null);

        createProduct("Tetra Tratamiento agua básico", "Mantiene agua limpia", new BigDecimal("8.99"), 60, false, "5x5x10 cm", "Tetra", "productos/peces/tetra-agua.jpg", saludPeces, "Tetra", "Tratamientos", null);
        createProduct("JBL Tratamiento anti algas", "Elimina algas", new BigDecimal("11.99"), 35, false, "5x5x10 cm", "JBL", "productos/peces/jbl-algas.jpg", saludPeces, "JBL", "Tratamientos", null);
        createProduct("Sera Tratamiento bacterias", "Control bacterias", new BigDecimal("10.50"), 20, false, "5x5x10 cm", "Sera", "productos/peces/sera-bacterias.jpg", saludPeces, "Sera", "Tratamientos", null);
        createProduct("Fluval Tratamiento completo", "Cuidado total", new BigDecimal("13.99"), 1, true, "5x5x10 cm", "Fluval", "productos/peces/fluval-tratamiento.jpg", saludPeces, "Fluval", "Tratamientos", null);

        createProduct("Tetra Comida básica peces", "Alimento diario", new BigDecimal("10.99"), 40, false, "10x5x3 cm", "Tetra", "productos/peces/tetra-basico.jpg", comidaPeces, "Tetra", "Escamas", null);
        createProduct("JBL Filtro compacto", "Filtro eficiente", new BigDecimal("25.99"), 15, false, "15x10x5 cm", "JBL", "productos/peces/jbl-compacto.jpg", accesoriosPeces, "JBL", "Filtros", null);
        createProduct("Sera Tratamiento agua", "Mejora calidad", new BigDecimal("9.99"), 1, true, "5x5x10 cm", "Sera", "productos/peces/sera-agua.jpg", saludPeces, "Sera", "Tratamientos", null);

        createProduct("Fluval Comida peces premium", "Nutrición avanzada", new BigDecimal("13.49"), 30, false, "10x5x3 cm", "Fluval", "productos/peces/fluval-comida.jpg", comidaPeces, "Fluval", "Escamas", null);
        createProduct("Tetra Decoración acuario roca", "Decoración natural", new BigDecimal("19.99"), 15, false, "20x10 cm", "Tetra", "productos/peces/roca.jpg", accesoriosPeces, "Tetra", "Decoración", null);
        //createProduct("JBL Test calidad agua", "Control parámetros", new BigDecimal("22.99"), 10, false, "10x5 cm", "JBL", "productos/peces/test.jpg", saludPeces, "JBL", "Control", null);
        createProduct("Sera Aireador acuario", "Oxigenación", new BigDecimal("17.99"), 1, true, "10x10 cm", "Sera", "productos/peces/aireador.jpg", accesoriosPeces, "Sera", "Acuarios", null);

        createProduct("Sera Escamas tropicales", "Alimento completo", new BigDecimal("11.49"), 30, false, "10x5x3 cm", "Sera", "productos/peces/escamas-tropicales.jpg", comidaPeces, "Sera", "Escamas", null);
        createProduct("JBL Comida granulada peces", "Granulado nutritivo", new BigDecimal("13.99"), 25, false, "10x5x3 cm", "JBL", "productos/peces/granulado.jpg", comidaPeces, "JBL", "Granulado", null);
        createProduct("Fluval Filtro interno compacto", "Filtración eficiente", new BigDecimal("29.99"), 15, false, "15x10 cm", "Fluval", "productos/peces/filtro-interno.jpg", accesoriosPeces, "Fluval", "Filtros", null);
        createProduct("Tetra Acondicionador agua", "Elimina cloro", new BigDecimal("9.99"), 20, true, "10x5 cm", "Tetra", "productos/peces/acondicionador.jpg", saludPeces, "Tetra", "Tratamientos", null);
    }

    private void seedPajaros() {
        Category comidaPajaros = categoryRepository.findBySlugAndParent_Slug("comida", "pajaros").orElseThrow();
        Category accesoriosPajaros = categoryRepository.findBySlugAndParent_Slug("accesorios", "pajaros").orElseThrow();
        //Category juguetesPajaros = categoryRepository.findBySlugAndParent_Slug("juguetes", "pajaros").orElseThrow();
        //Category saludPajaros = categoryRepository.findBySlugAndParent_Slug("salud", "pajaros").orElseThrow();
        Category higienePajaros = categoryRepository.findBySlugAndParent_Slug("higiene", "pajaros").orElseThrow();

// 🔹 COMIDA
        createProduct("Psittacus Pienso completo aves", "Alimento equilibrado", new BigDecimal("14.99"), 40, false, "20x10x10 cm", "Psittacus", "productos/pajaros/pienso.jpg", comidaPajaros, "Psittacus", "Pienso", null);
        createProduct("Versele Laga Semillas premium", "Mezcla nutritiva", new BigDecimal("12.99"), 50, false, "20x10x10 cm", "Versele Laga", "productos/pajaros/semillas.jpg", comidaPajaros, "Versele Laga", "Semillas", null);
        createProduct("Vitakraft Snacks miel", "Snack energético", new BigDecimal("6.99"), 60, true, "10x5x5 cm", "Vitakraft", "productos/pajaros/snacks.jpg", comidaPajaros, "Vitakraft", "Snacks", null);
        createProduct("Psittacus Pasta de cría", "Ideal para cría", new BigDecimal("15.99"), 30, false, "20x10x10 cm", "Psittacus", "productos/pajaros/pasta-cria.jpg", comidaPajaros, "Psittacus", "Pasta", null);

// 🔹 ACCESORIOS
        createProduct("Ferplast Jaula aves básica", "Jaula espaciosa", new BigDecimal("59.99"), 10, false, "60x40x40 cm", "Ferplast", "productos/pajaros/jaula.jpg", accesoriosPajaros, "Ferplast", "Jaulas", null);
        createProduct("Trixie Bebedero aves", "Bebedero práctico", new BigDecimal("5.99"), 45, false, "10x5 cm", "Trixie", "productos/pajaros/bebedero.jpg", accesoriosPajaros, "Trixie", "Comederos y Bebederos", null);
        createProduct("Ferplast Comedero aves", "Comedero resistente", new BigDecimal("4.99"), 50, false, "10x5 cm", "Ferplast", "productos/pajaros/comedero.jpg", accesoriosPajaros, "Ferplast", "Comederos y Bebederos", null);

// 🔹 JUGUETES
//        createProduct("Trixie Columpio aves", "Juguete colgante", new BigDecimal("8.99"), 35, false, "15x10 cm", "Trixie", "productos/pajaros/columpio.jpg", juguetesPajaros, "Trixie", "Columpios", null);
//        createProduct("Vitakraft Juguete madera", "Para picoteo", new BigDecimal("7.99"), 30, false, "15x10 cm", "Vitakraft", "productos/pajaros/juguete-madera.jpg", juguetesPajaros, "Vitakraft", "Mordedores", null);
//        createProduct("Psittacus Juguete interactivo", "Estimulación mental", new BigDecimal("12.99"), 1, true, "20x10 cm", "Psittacus", "productos/pajaros/interactivo.jpg", juguetesPajaros, "Psittacus", "Interactivos", null);

// 🔹 SALUD
//        createProduct("Beaphar Vitaminas aves", "Suplemento vitamínico", new BigDecimal("9.99"), 25, false, "5x5x10 cm", "Beaphar", "productos/pajaros/vitaminas.jpg", saludPajaros, "Beaphar", "Suplementos", null);
//        createProduct("Psittacus Tratamiento digestivo", "Mejora digestión", new BigDecimal("11.99"), 20, false, "5x5x10 cm", "Psittacus", "productos/pajaros/digestivo.jpg", saludPajaros, "Psittacus", "Tratamientos", null);
//        createProduct("Versele Laga Cuidado plumaje", "Salud del plumaje", new BigDecimal("10.49"), 18, false, "5x5x10 cm", "Versele Laga", "productos/pajaros/plumaje.jpg", saludPajaros, "Versele Laga", "Suplementos", null);
//        createProduct("Beaphar Tratamiento completo aves", "Cuidado integral", new BigDecimal("13.99"), 12, true, "5x5x10 cm", "Beaphar", "productos/pajaros/tratamiento.jpg", saludPajaros, "Beaphar", "Tratamientos", null);

// 🔹 HIGIENE
        //createProduct("Vitakraft Arena jaula", "Absorción eficaz", new BigDecimal("6.99"), 50, false, "30x20x10 cm", "Vitakraft", "productos/pajaros/arena.jpg", higienePajaros, "Vitakraft", "Arena", null);
        createProduct("Trixie Papel higiénico aves", "Base limpia", new BigDecimal("5.49"), 40, false, "30x20x10 cm", "Trixie", "productos/pajaros/papel.jpg", higienePajaros, "Trixie", "Higiene", null);
        createProduct("Ferplast Lecho aves", "Material absorbente", new BigDecimal("7.99"), 35, false, "30x20x10 cm", "Ferplast", "productos/pajaros/lecho.jpg", higienePajaros, "Ferplast", "Lechos", null);
        createProduct("Versele Laga Higiene premium", "Alta calidad", new BigDecimal("9.99"), 1, true, "30x20x10 cm", "Versele Laga", "productos/pajaros/higiene.jpg", higienePajaros, "Versele Laga", "Higiene", null);

        createProduct("Psittacus Snack frutas aves", "Snack natural", new BigDecimal("7.99"), 30, false, "10x5 cm", "Psittacus", "productos/pajaros/snack-frutas.jpg", comidaPajaros, "Psittacus", "Snacks", null);
        createProduct("Trixie Percha madera", "Apoyo natural", new BigDecimal("6.49"), 40, false, "20x5 cm", "Trixie", "productos/pajaros/percha.jpg", accesoriosPajaros, "Trixie", "Perchas", null);
        createProduct("Vitakraft Baño aves", "Higiene diaria", new BigDecimal("8.99"), 25, false, "15x10 cm", "Vitakraft", "productos/pajaros/bano.jpg", higienePajaros, "Vitakraft", "Higiene", null);
        //createProduct("Ferplast Juguete espejo", "Estimulación", new BigDecimal("5.99"), 1, true, "10x5 cm", "Ferplast", "productos/pajaros/espejo.jpg", juguetesPajaros, "Ferplast", "Interactivos", null);

        createProduct("Versele Laga Mix semillas aves", "Mezcla variada", new BigDecimal("11.99"), 35, false, "20x10x10 cm", "Versele Laga", "productos/pajaros/mix-semillas.jpg", comidaPajaros, "Versele Laga", "Semillas", null);
        createProduct("Psittacus Pienso mantenimiento", "Dieta equilibrada", new BigDecimal("15.49"), 25, false, "20x10x10 cm", "Psittacus", "productos/pajaros/pienso-mantenimiento.jpg", comidaPajaros, "Psittacus", "Pienso", null);
        createProduct("Trixie Comedero exterior aves", "Fácil instalación", new BigDecimal("7.99"), 40, false, "15x10 cm", "Trixie", "productos/pajaros/comedero-exterior.jpg", accesoriosPajaros, "Trixie", "Comederos y Bebederos", null);
        //createProduct("Vitakraft Arena absorbente aves", "Control de olores", new BigDecimal("6.49"), 45, true, "30x20x10 cm", "Vitakraft", "productos/pajaros/arena-absorbente.jpg", higienePajaros, "Vitakraft", "Arena", null);

        createProduct("Vitakraft Mix frutas aves", "Alimento variado", new BigDecimal("10.99"), 30, false, "20x10x10 cm", "Vitakraft", "productos/pajaros/mix-frutas.jpg", comidaPajaros, "Vitakraft", "Semillas", null);
        createProduct("Psittacus Pienso alta energía", "Para aves activas", new BigDecimal("16.49"), 20, false, "20x10x10 cm", "Psittacus", "productos/pajaros/pienso-energia.jpg", comidaPajaros, "Psittacus", "Pienso", null);
        createProduct("Ferplast Bebedero exterior", "Fácil instalación", new BigDecimal("6.99"), 35, false, "10x5 cm", "Ferplast", "productos/pajaros/bebedero-exterior.jpg", accesoriosPajaros, "Ferplast", "Comederos y Bebederos", null);
        createProduct("Trixie Base higiénica jaula", "Control suciedad", new BigDecimal("5.99"), 45, true, "30x20x10 cm", "Trixie", "productos/pajaros/base-higienica.jpg", higienePajaros, "Trixie", "Higiene", null);

        // Limpieza
        createProduct("Vitakraft Spray limpieza jaulas", "Limpieza rápida", new BigDecimal("7.50"), 20, false, "250ml", "Vitakraft", "productos/pajaros/limpieza1.jpg", higienePajaros, "Vitakraft", "Limpieza", null);

        // Lechos (1)
        createProduct("Arquivet Lecho natural aves", "Base absorbente", new BigDecimal("5.99"), 30, false, "1kg", "Arquivet", "productos/pajaros/lecho1.jpg", higienePajaros, "Arquivet", "Lechos", null);
    }

    private void seedReptiles() {
        Category comidaReptiles = categoryRepository.findBySlugAndParent_Slug("comida", "reptiles").orElseThrow();
        Category accesoriosReptiles = categoryRepository.findBySlugAndParent_Slug("accesorios", "reptiles").orElseThrow();
        Category saludReptiles = categoryRepository.findBySlugAndParent_Slug("salud", "reptiles").orElseThrow();
        //Category higieneReptiles = categoryRepository.findBySlugAndParent_Slug("higiene", "reptiles").orElseThrow();

// 🔹 COMIDA
        createProduct("Exo Terra Grillos secos", "Alimento natural", new BigDecimal("9.99"), 40, false, "10x5x5 cm", "Exo Terra", "productos/reptiles/grillos.jpg", comidaReptiles, "Exo Terra", "Insectos", null);
        createProduct("Tetra ReptoMin sticks", "Comida para tortugas", new BigDecimal("11.99"), 35, false, "10x5x5 cm", "Tetra", "productos/reptiles/reptomin.jpg", comidaReptiles, "Tetra", "Sticks", null);
        createProduct("JBL Insectos deshidratados", "Proteína alta", new BigDecimal("12.49"), 30, false, "10x5x5 cm", "JBL", "productos/reptiles/insectos.jpg", comidaReptiles, "JBL", "Insectos", null);
        createProduct("Exo Terra Pienso reptiles", "Alimento completo", new BigDecimal("13.99"), 25, true, "10x5x5 cm", "Exo Terra", "productos/reptiles/pienso.jpg", comidaReptiles, "Exo Terra", "Pienso", null);
        createProduct("Tetra Gammarus natural", "Snack nutritivo", new BigDecimal("10.49"), 20, false, "10x5x5 cm", "Tetra", "productos/reptiles/gammarus.jpg", comidaReptiles, "Tetra", "Snacks", null);

// 🔹 ACCESORIOS
        createProduct("Exo Terra Terrario básico", "Terrario compacto", new BigDecimal("79.99"), 10, false, "50x30x30 cm", "Exo Terra", "productos/reptiles/terrario.jpg", accesoriosReptiles, "Exo Terra", "Terrarios", null);
        createProduct("JBL Lámpara UVB", "Iluminación esencial", new BigDecimal("34.99"), 20, false, "20x5 cm", "JBL", "productos/reptiles/uvb.jpg", accesoriosReptiles, "JBL", "Iluminación", null);
        createProduct("Exo Terra Calefactor roca", "Fuente de calor", new BigDecimal("29.99"), 15, false, "15x10 cm", "Exo Terra", "productos/reptiles/calefactor.jpg", accesoriosReptiles, "Exo Terra", "Calefacción", null);
        //createProduct("Tetra Termómetro terrario", "Control temperatura", new BigDecimal("9.99"), 30, false, "5x5 cm", "Tetra", "productos/reptiles/termometro.jpg", accesoriosReptiles, "Tetra", "Control", null);
        createProduct("JBL Sistema niebla", "Humedad adecuada", new BigDecimal("49.99"), 8, true, "20x15 cm", "JBL", "productos/reptiles/niebla.jpg", accesoriosReptiles, "JBL", "Humedad", null);

// 🔹 SALUD
        createProduct("Exo Terra Calcio reptiles", "Suplemento mineral", new BigDecimal("8.99"), 25, false, "5x5x10 cm", "Exo Terra", "productos/reptiles/calcio.jpg", saludReptiles, "Exo Terra", "Suplementos", null);
        createProduct("Tetra Vitaminas reptiles", "Refuerzo salud", new BigDecimal("9.99"), 20, false, "5x5x10 cm", "Tetra", "productos/reptiles/vitaminas.jpg", saludReptiles, "Tetra", "Suplementos", null);
        createProduct("JBL Tratamiento digestivo", "Mejora digestión", new BigDecimal("10.99"), 18, false, "5x5x10 cm", "JBL", "productos/reptiles/digestivo.jpg", saludReptiles, "JBL", "Tratamientos", null);
        createProduct("Exo Terra Tratamiento completo", "Cuidado integral", new BigDecimal("13.99"), 12, true, "5x5x10 cm", "Exo Terra", "productos/reptiles/tratamiento.jpg", saludReptiles, "Exo Terra", "Tratamientos", null);

// 🔹 HIGIENE
//        createProduct("Exo Terra Sustrato natural", "Base para terrario", new BigDecimal("14.99"), 40, false, "30x20x10 cm", "Exo Terra", "productos/reptiles/sustrato.jpg", higieneReptiles, "Exo Terra", "Sustrato", null);
//        createProduct("JBL Arena desértica", "Ideal reptiles desierto", new BigDecimal("12.99"), 35, false, "30x20x10 cm", "JBL", "productos/reptiles/arena.jpg", higieneReptiles, "JBL", "Arena", null);
//        createProduct("Tetra Sustrato húmedo", "Ambientes tropicales", new BigDecimal("13.49"), 30, false, "30x20x10 cm", "Tetra", "productos/reptiles/sustrato-humedo.jpg", higieneReptiles, "Tetra", "Sustrato", null);
//        createProduct("Exo Terra Higiene premium", "Alta absorción", new BigDecimal("15.99"), 20, false, "30x20x10 cm", "Exo Terra", "productos/reptiles/higiene.jpg", higieneReptiles, "Exo Terra", "Higiene", null);
//        createProduct("JBL Sustrato ecológico", "Material natural", new BigDecimal("11.99"), 1, true, "30x20x10 cm", "JBL", "productos/reptiles/sustrato-eco.jpg", higieneReptiles, "JBL", "Sustrato", null);

        //createProduct("JBL Pinzas alimentación", "Para alimentar reptiles", new BigDecimal("9.99"), 30, false, "20x3 cm", "JBL", "productos/reptiles/pinzas.jpg", accesoriosReptiles, "JBL", "Accesorios", null);
        createProduct("Exo Terra Cueva refugio", "Escondite natural", new BigDecimal("16.99"), 20, false, "20x15 cm", "Exo Terra", "productos/reptiles/cueva.jpg", accesoriosReptiles, "Exo Terra", "Refugios", null);
        //createProduct("Tetra Spray humedad", "Control ambiental", new BigDecimal("8.49"), 25, false, "10x5 cm", "Tetra", "productos/reptiles/spray.jpg", saludReptiles, "Tetra", "Humedad", null);
        createProduct("Exo Terra Planta decorativa", "Decoración terrario", new BigDecimal("12.99"), 1, true, "20x10 cm", "Exo Terra", "productos/reptiles/planta.jpg", accesoriosReptiles, "Exo Terra", "Decoración", null);

        createProduct("Exo Terra Alimento tortugas", "Comida equilibrada", new BigDecimal("12.99"), 30, false, "10x5x5 cm", "Exo Terra", "productos/reptiles/alimento-tortugas.jpg", comidaReptiles, "Exo Terra", "Pienso", null);
        createProduct("JBL Insectos naturales secos", "Fuente de proteína", new BigDecimal("11.49"), 25, false, "10x5x5 cm", "JBL", "productos/reptiles/insectos-secos.jpg", comidaReptiles, "JBL", "Insectos", null);
        createProduct("Tetra Lámpara calor reptiles", "Calor constante", new BigDecimal("27.99"), 15, false, "20x5 cm", "Tetra", "productos/reptiles/lampara-calor.jpg", accesoriosReptiles, "Tetra", "Iluminación", null);
        createProduct("Exo Terra Suplemento vitaminas", "Refuerzo nutricional", new BigDecimal("9.49"), 20, true, "5x5x10 cm", "Exo Terra", "productos/reptiles/vitaminas.jpg", saludReptiles, "Exo Terra", "Suplementos", null);

        createProduct("Tetra Alimento completo reptiles", "Nutrición equilibrada", new BigDecimal("11.99"), 30, false, "10x5x5 cm", "Tetra", "productos/reptiles/alimento-completo.jpg", comidaReptiles, "Tetra", "Pienso", null);
        createProduct("JBL Grillos vivos conservados", "Proteína natural", new BigDecimal("13.49"), 25, false, "10x5x5 cm", "JBL", "productos/reptiles/grillos-vivos.jpg", comidaReptiles, "JBL", "Insectos", null);
        //createProduct("Exo Terra Termómetro digital", "Control preciso", new BigDecimal("18.99"), 20, false, "10x5 cm", "Exo Terra", "productos/reptiles/termometro-digital.jpg", accesoriosReptiles, "Exo Terra", "Control", null);
        createProduct("JBL Suplemento calcio plus", "Refuerzo óseo", new BigDecimal("9.99"), 18, true, "5x5x10 cm", "JBL", "productos/reptiles/calcio-plus.jpg", saludReptiles, "JBL", "Suplementos", null);

        // Sticks
        createProduct("Tetra Sticks tortugas", "Alimento en sticks", new BigDecimal("9.99"), 25, false, "250g", "Tetra", "productos/reptiles/sticks1.jpg", comidaReptiles, "Tetra", "Sticks", null);

        // Snacks
        createProduct("JBL Snacks reptiles proteína", "Snack energético", new BigDecimal("6.99"), 30, false, "100g", "JBL", "productos/reptiles/snack1.jpg", comidaReptiles, "JBL", "Snacks", null);

        // Terrarios
        createProduct("Exo Terra Terrario cristal", "Terrario completo", new BigDecimal("89.99"), 8, false, "60cm", "Exo Terra", "productos/reptiles/terrario1.jpg", accesoriosReptiles, "Exo Terra", "Terrarios", null);

        // Refugio
        createProduct("Trixie Refugio roca reptiles", "Escondite natural", new BigDecimal("12.99"), 20, false, "15cm", "Trixie", "productos/reptiles/refugio1.jpg", accesoriosReptiles, "Trixie", "Refugios", null);

        // Decoración
        createProduct("Exo Terra Planta decorativa", "Decoración realista", new BigDecimal("11.99"), 18, false, "25cm", "Exo Terra", "productos/reptiles/decoracion1.jpg", accesoriosReptiles, "Exo Terra", "Decoración", null);
    }

    private Product createProduct(
            String name,
            String description,
            BigDecimal price,
            int stock,
            boolean solidaryProduct,
            String dimensions,
            String provider,
            String imageUrl,
            Category category,
            String marca,
            String tipo,
            String edad
    ) {

        Product product = new Product();

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setSolidaryProduct(solidaryProduct);
        product.setDimensions(dimensions);
        product.setProvider(provider);
        product.setImageUrl(imageUrl);
        product.setCategory(category);

        // Slug
        String baseSlug = product.generateSlug(name);
        product.setSlug(productService.generateUniqueSlug(baseSlug));

        // Tags (opcionales)
        addTagIfNotNull(product, "marca", marca);
        addTagIfNotNull(product, "tipo", tipo);
        addTagIfNotNull(product, "edad", edad);

        return productRepository.save(product);
    }

    private void addTagIfNotNull(Product product, String type, String value) {
        if (value != null && !value.isBlank()) {
            product.addTag(tagService.getOrCreate(type, value));
        }
    }
}