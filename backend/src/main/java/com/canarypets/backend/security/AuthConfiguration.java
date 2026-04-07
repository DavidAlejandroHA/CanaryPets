package com.canarypets.backend.security;

import com.canarypets.backend.repositories.RoleRepository;
import com.canarypets.backend.repositories.UserRepository;
import com.canarypets.backend.seeders.UsersAndRolesSeeder;
import com.canarypets.backend.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthConfiguration {

    @Autowired
    UserService userDetailsService;

    // Por algun motivo no funciona
    /*@Bean
    public UserDetailsService userDetailsService() {
        return new UserService(); // Defines the custom UserDetailsService bean
    }*/

    @Bean // Se establece cuál será el password encoder pro defecto a utilizar por spring
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Importante: Filtro para forzar encoding antes de que Spring toque parámetros
        // Hay que añadirlo aquí porque se suele ejecutar demasiado tarde por defecto si no se define aquí
        httpSecurity.addFilterBefore(new Utf8RequestFilter(), UsernamePasswordAuthenticationFilter.class);

        //Filtro para evitar que usuarios autenticados puedan acceder al formulario de login al introducir la url
        // Importante definirlo antes que el resto de métodos
        //httpSecurity.addFilterBefore(new LoginPageFilter(), UsernamePasswordAuthenticationFilter.class);
        // Lo mismo pero para /register
        //httpSecurity.addFilterBefore(new RegisterPageFilter(), UsernamePasswordAuthenticationFilter.class);
        //Nota: también se puede cambiar por DefaultLoginPageGeneratingFilter.class);


        // Definición de endpoints accesibles
        httpSecurity.authorizeHttpRequests(requests -> {
            // Definir endpoints accesibles para todos los usuarios, es decir, que no requieran autentificación
            requests.requestMatchers("/premium/**").permitAll()
            .requestMatchers("/", "/home", "/auth/login", "/auth/register",
                            "/auth/logout", "/css/**", "/js/**",
                            "/categoria/**", "/categoria", "/images/**",
                            "/producto/**").permitAll()
                    .anyRequest().authenticated();
        });

        // https://stackoverflow.com/questions/39166920/spring-security-userdetailsservice-not-working
        // Importante: para definir el tipo de userDetailsService que utiliza spring security, se asocia
        // con el método_ .userDetailsService() usando el userDetailsService asociado con el @Autowired, que es
        // en este caso el UserService que contiene la implementación de dicha interfaz
        httpSecurity.userDetailsService(userDetailsService);

        // Definición del comportamiento del login
        httpSecurity.formLogin(login -> {
            login.loginPage("/auth/login")
                    .loginProcessingUrl("/auth/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .permitAll();
        });

        // Definición del comportamiento del logout
        httpSecurity.logout(logout -> {
            logout.logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/auth/login?logout").permitAll();
        });

        return httpSecurity.build();
    }

    @Bean
    public String seedUsersAndRoles(UserRepository userRepository,
                                    RoleRepository roleRepository) {
        UsersAndRolesSeeder.seedUsersAndRoles(userRepository, roleRepository);
        return "Usuarios y roles creados";
    }

    @Bean // Forzar que TODAS las requests usen UTF-8 antes de que lleguen a Spring MVC
    // Este y otros cambios ayudan a evitar un error MalformedInputException al enviar ciertos carácteres
    public FilterRegistrationBean<CharacterEncodingFilter> encodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        FilterRegistrationBean<CharacterEncodingFilter> registrationBean =
                new FilterRegistrationBean<>(filter);

        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    /*@Bean
    @Transactional // Importante
    // https://stackoverflow.com/questions/77305942/spring-boot-hibernate-error-detached-entity-passed-to-persist-anyone-know-how
    public String seedTermsAndExamples(ExampleRepository exampleRepository,
                                       TermRepository termRepository) {
        TermsAndExamplesSeeder.seedTermsAndExamples(exampleRepository, termRepository);
        return "Terms y examples creados";
    }*/
}
