package com.canarypets.backend.seeders;

import com.canarypets.backend.models.Role;
import com.canarypets.backend.models.User;
import com.canarypets.backend.repositories.RoleRepository;
import com.canarypets.backend.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class UsersAndRolesSeeder {
    public static void seedUsersAndRoles(UserRepository userRepository, RoleRepository roleRepository) {
        // Obtener roles del repositorio de roles
        Role roleBasic = roleRepository.findByName("ROLE_BASIC");
        Role rolePremium = roleRepository.findByName("ROLE_PREMIUM");
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");

        // Si no existen los roles, se crean
        //if (roleUser == null) { roleRepository.save(new Role("ROLE_USER")); }
        if (roleBasic == null) { roleBasic = roleRepository.save(new Role("ROLE_BASIC")); }
        if (rolePremium == null) { rolePremium = roleRepository.save(new Role("ROLE_PREMIUM")); }
        if (roleAdmin == null) { roleAdmin =  roleRepository.save(new Role("ROLE_ADMIN")); }

        // Crear usuarios basic, premium y admin
        User userBasic = new User("basic", new BCryptPasswordEncoder().encode("basic1234"),
                "basic@gmail.com", "Calle Brava, El Corcho", "11111", List.of(roleBasic), "Spain");
        User userPremium = new User("premium", new BCryptPasswordEncoder().encode("premium1234"),
                "premium@gmail.com", "Calle Cancha, La Carta", "22222", List.of(rolePremium), "Spain");
        User userAdmin = new User("admin", new BCryptPasswordEncoder().encode("admin1234"),
                "admin@gmail.com", "Calle Poncho, Guadalajara", "33333", List.of(roleAdmin), "Spain");

        createUser(userBasic, userRepository);
        createUser(userPremium, userRepository);
        createUser(userAdmin, userRepository);
    }

    /**
     * Creación de usuarios
     * @param user
     * @param userRepository
     */
    private static void createUser(User user, UserRepository userRepository) {
        if (userRepository.findByEmail(user.getEmail()) == null || userRepository.findByNickName(user.getNickName()) == null) {
            userRepository.save(user);
            System.out.println("El usuario " + user.getNickName() + " ha sido creado");
        } else {
            System.out.println("El usuario " + user.getNickName() + " ya ha sido creado previamente");
        }
        // Nota: la creación de un usuario también se puede hacer sin comprobaciones dentro de un try-catch, ya que al
        // intentar crearlo de nuevo se lanza un error debido a que la columna nickName tiene la restricción unique = true

        //try { } catch (Exception e) { }
    }
}
