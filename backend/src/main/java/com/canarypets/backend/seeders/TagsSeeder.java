package com.canarypets.backend.seeders;

import com.canarypets.backend.models.Tag;
import com.canarypets.backend.repositories.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
public class TagsSeeder implements CommandLineRunner {

    private final TagRepository tagRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (tagRepository.count() == 0) {
            seedTags();
        }
    }

    private void seedTags() {
        // Tipos
        createTag("type", "Pienso");
        createTag("type", "Comida húmeda", "humeda");
        createTag("type", "Snacks");
        createTag("type", "Dieta veterinaria", "dieta");

        createTag("type", "Camas");
        createTag("type", "Transportines");
        createTag("type", "Correas y arneses");
        createTag("type", "Comederos y bebederos");

        createTag("type", "Mordedores");
        createTag("type", "Interactivos");

        createTag("type", "Antiparasitarios");
        createTag("type", "Suplementos");

        createTag("type", "Champús", "champus");
        createTag("type", "Cepillos");
        createTag("type", "Toallitas");

        createTag("type", "Areneros");
        createTag("type", "Rascadores");
        createTag("type", "Pelotas");
        createTag("type", "Ratones");
        createTag("type", "Arenas");

        createTag("type", "Heno");
        createTag("type", "Jaulas");
        createTag("type", "Refugios");
        /*createTag("type", "bebederos");*/
        createTag("type", "Lechos");
        createTag("type", "Limpieza");

        createTag("type", "Escamas");
        createTag("type", "Granulado");
        createTag("type", "Acuarios y accesorios", "acuarios");
        createTag("type", "Decoración");
        createTag("type", "Filtros");
        //createTag("type", "Iluminación");
        createTag("type", "Tratamientos agua","tratamientos");

        createTag("type", "Semillas");
        createTag("type", "Pasta");
        createTag("type", "Perchas");

        createTag("type", "Insectos");
        createTag("type", "Humedad");
        createTag("type", "Sticks");
        createTag("type", "Terrarios");
        createTag("type", "Calefacción");
        createTag("type", "Iluminación UV", "iluminacion");

        // Marcas
        createTag("marca", "Royal Canin");
        createTag("marca", "Advance");
        createTag("marca", "Purina Pro Plan");
        createTag("marca", "Acana");
        createTag("marca", "Purina One");
        createTag("marca", "Hill’s");
        createTag("marca", "Vitakraft");
        createTag("marca", "Versele-Laga");
        createTag("marca", "Cunipic");
        createTag("marca", "JR Farm");
        createTag("marca", "Psittacus");
        createTag("marca", "Kaytee");
        createTag("marca", "Tetra");
        createTag("marca", "JBL");
        createTag("marca", "Sera");
        createTag("marca", "Fluval");
        createTag("marca", "Exo Terra");
        createTag("marca", "Zoo Med");
        createTag("marca", "Arcadia");

        //🐶 Perros
        //Royal Canin
        //
        //Advance
        //
        //Purina Pro Plan
        //
        //Acana
        //
        //🐱 Gatos
        //Royal Canin
        //
        //Purina One
        //
        //Hill’s
        //
        //Advance
        //
        //👉 (ves que se repiten → esto es normal en ecommerce)
        //
        //🐹 Roedores
        //Vitakraft
        //
        //Versele-Laga
        //
        //Cunipic
        //
        //JR Farm
        //
        //🐦 Pájaros
        //Versele-Laga
        //
        //Vitakraft
        //
        //Psittacus
        //
        //Kaytee
        //
        //🐠 Peces
        //Tetra
        //
        //JBL
        //
        //Sera
        //
        //Fluval
        //
        //🦎 Reptiles
        //Exo Terra
        //
        //Zoo Med
        //
        //JBL
        //
        //Arcadia

        // Edades
        createTag("edad", "Cachorro");
        createTag("edad", "Adulto");
        createTag("edad", "Senior");
    }

    private Tag createTag(String type, String name) {
        Tag tag = new Tag();
        tag.setType(type);
        tag.setName(name);
        tag.generateSlug();
        return tagRepository.save(tag);
    }

    private Tag createTag(String type, String name, String slug) {
        Tag tag = new Tag();
        tag.setType(type);
        tag.setName(name);
        tag.generateSlug(slug);
        //tag.setSlug(slug);
        return tagRepository.save(tag);
    }
}