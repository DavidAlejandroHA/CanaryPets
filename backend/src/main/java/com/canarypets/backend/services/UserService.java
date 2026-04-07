package com.canarypets.backend.services;

import com.canarypets.backend.models.Role;
import com.canarypets.backend.models.User;
import com.canarypets.backend.repositories.RoleRepository;
import com.canarypets.backend.repositories.UserRepository;
import com.canarypets.backend.security.AuthConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService/*, EntityService<User>*/ {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    /**
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        // Si el usuario existe se retorna el usuario logeado
        if (user != null) {
            List<GrantedAuthority> authorities = getUserAuthorities(user);

            return new org.springframework.security.core.userdetails.User( // Retornar UserDetails
                    user.getEmail(),
                    user.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    authorities
            );
        } else {
            throw new UsernameNotFoundException("Invalid email or password!");
        }
        //return null;
    }

    public List<GrantedAuthority> getUserAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        // Obtener lista de authorities en base a los roles del usuario
    }


    public void addRole(String username, String roleName) {
        User user = userRepository.findByEmail(username);
        Role role = roleRepository.findByName("ROLE_" + roleName);

        List<Role> userRoles = user.getRoles();
        if (role == null) {
            roleRepository.save(new Role("ROLE_" + roleName));
        }
        if (!user.getRoles().contains(role)) {
            userRoles.add(role);
            user.setRoles(userRoles);
        }

        userRepository.save(user);
    }

    /**
     * Guarda el usuario especificado con el rol por defecto ROLE_USER y encripta su contraseña
     *
     * @param user El usuario a guardar
     */
    public void saveUser(User user) {
        Role role = roleRepository.findByName("ROLE_BASIC");

        if (role == null) { // Se crea el rol para usuarios si es que no existe previamente
            role = roleRepository.save(new Role("ROLE_BASIC"));
        }
        // Se encripta la contraseña del usuario y se le asignan los roles (ROLE_USER)
        user.setPassword(AuthConfiguration.passwordEncoder().encode(user.getPassword()));
        user.setRoles(List.of(role));

        userRepository.save(user);
    }

    /**
     * Busca un usuario cuyo email sea el mismo que el especificado
     *
     * @param email El email a buscar
     * @return El usuario encontrado
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User getUserByNickName(String nickName) {
        return userRepository.findByNickName(nickName);
    }
}
