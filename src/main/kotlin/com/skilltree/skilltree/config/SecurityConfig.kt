package com.skilltree.skilltree.config

import com.skilltree.skilltree.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Service

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    // Public auth endpoints
                    .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/logout").permitAll()
                    // H2 console (development only)
                    .requestMatchers("/h2-console/**").permitAll()

                    // UI Applications
                    // Root path
                    .requestMatchers("/").permitAll()
                    // Public app static resources - accessible to everyone
                    .requestMatchers("/public/**").permitAll()
                    // Login and signup pages
                    .requestMatchers("/login", "/signup").permitAll()
                    // Private app static resources - requires authentication
                    .requestMatchers("/private/**").authenticated()
                    // Private app endpoints - requires authentication
                    .requestMatchers("/app", "/app/**").authenticated()
                    // Admin app static resources and endpoints - requires ADMIN role
                    .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")

                    // GET requests on resource endpoints are public (read access)
                    .requestMatchers(HttpMethod.GET, "/api/domains/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/subcategories/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/skills/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/microskills/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/sources/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/curricula/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/resources/**").permitAll()

                    // POST/PUT/DELETE on resource endpoints require ADMIN role
                    .requestMatchers(HttpMethod.POST, "/api/domains/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/domains/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/domains/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST, "/api/subcategories/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/subcategories/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/subcategories/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST, "/api/skills/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/skills/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/skills/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST, "/api/microskills/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/microskills/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/microskills/**").hasRole("ADMIN")

                    // Curriculum endpoints require ADMIN for modifications
                    .requestMatchers(HttpMethod.POST, "/api/curricula/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/curricula/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/curricula/**").hasRole("ADMIN")

                    // Resource endpoints require ADMIN for modifications
                    .requestMatchers(HttpMethod.POST, "/api/resources/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/resources/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/resources/**").hasRole("ADMIN")

                    // Generation endpoints require ADMIN role
                    .requestMatchers("/api/generation/**").hasRole("ADMIN")

                    // /me endpoint requires authentication
                    .requestMatchers("/api/auth/me").authenticated()
                    // All other requests require authentication
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/app/", true)
            }
            .logout { logout ->
                logout
                    .logoutSuccessUrl("/")
                    .permitAll()
            }
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }
            .sessionManagement { session ->
                session.maximumSessions(1)
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        // Try to find by username first, then by email
        val user = userRepository.findByUsername(username)
            .or { userRepository.findByEmail(username) }
            .orElseThrow { UsernameNotFoundException("User not found: $username") }

        return User.builder()
            .username(user.username)
            .password(user.password)
            .authorities(SimpleGrantedAuthority("ROLE_${user.role}"))
            .build()
    }
}
