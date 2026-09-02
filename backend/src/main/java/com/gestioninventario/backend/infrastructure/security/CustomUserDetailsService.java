package com.gestioninventario.backend.infrastructure.security;

import com.gestioninventario.backend.domain.entity.Usuario;
import com.gestioninventario.backend.infrastructure.persistence.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
            .findByCorreoElectronico(correo)
            .orElseThrow(() -> new UsernameNotFoundException("El usuario con correo " + correo + " no existe"));

        return User.builder()
            .username(usuario.getCorreo_electronico())
            .password(usuario.getContrasena())
            .authorities(
                new SimpleGrantedAuthority(
                    "ROLE_" + usuario.getRol().getNombre_rol()
                )
            )
            .build();
    }
}