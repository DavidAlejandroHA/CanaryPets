package com.canarypets.backend.services;

import com.canarypets.backend.models.Tag;
import com.canarypets.backend.repositories.TagRepository;
import com.canarypets.backend.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public Tag getOrCreate(String type, String name) {

        /*String slug = name.toLowerCase()
                .replace(" ", "-")
                .replace("’", "")
                .replace("'", "");*/
        String slug = SlugUtils.generateSlug(name);

        //String slug = SlugUtils.generateUniqueSlug(name, s -> tagRepository.existsBySlug(s));
        // Usar slug único (por si acaso) para evitar duplicados. Si hubiera un registro que se repitiera
        // Nota: esto duplica los tags a la hora de hacer seeding de productos, mejor no usarlo con los tags, solo en
        // nombre de productos

        return tagRepository.findByTypeAndSlug(type, slug)
                .orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setType(type);
                    tag.setName(name);
                    tag.setSlug(slug);
                    return tagRepository.save(tag);
                });
    }
}
