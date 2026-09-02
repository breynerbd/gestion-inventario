package com.gestioninventario.backend.application.service;

import com.gestioninventario.backend.application.dto.auth.LoginRequestDTO;
import com.gestioninventario.backend.application.dto.auth.LoginResponseDTO;
import com.gestioninventario.backend.application.dto.auth.RefreshTokenRequestDTO;
import com.gestioninventario.backend.application.dto.auth.RegistroRequestDTO;
import com.gestioninventario.backend.domain.entity.Rol;
import com.gestioninventario.backend.domain.entity.Usuario;
import com.gestioninventario.backend.domain.exception.CredencialesInvalidasException;
import com.gestioninventario.backend.domain.exception.RecursoDuplicadoException;
import com.gestioninventario.backend.domain.exception.RecursoNoEncontradoException;
import com.gestioninventario.backend.domain.exception.UsuarioBloqueadoException;
import com.gestioninventario.backend.domain.exception.UsuarioInactivoException;
import com.gestioninventario.backend.infrastructure.persistence.repository.RolRepository;
import com.gestioninventario.backend.infrastructure.persistence.repository.UsuarioRepository;
import com.gestioninventario.backend.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final int MAX_INTENTOS = 5;
    private static final String ROL_REGISTRO = "OPERADOR";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO registrarUsuario(RegistroRequestDTO registroRequest) {

        if (usuarioRepository.existsByCorreoElectronico(
                registroRequest.getCorreo_electronico())) {

            throw new RecursoDuplicadoException(
                    "El correo electrónico ya está registrado");
        }

        if (usuarioRepository.existsByNombreUsuario(
                registroRequest.getNombre_usuario())) {

            throw new RecursoDuplicadoException(
                    "El nombre de usuario ya está registrado");
        }

        Rol rol = rolRepository.findByNombreRol(ROL_REGISTRO)
            .orElseThrow(() -> new RecursoNoEncontradoException("El rol " + ROL_REGISTRO + " no existe"));

        Usuario usuario = new Usuario();

        usuario.setNombre_usuario(registroRequest.getNombre_usuario());
        usuario.setContrasena(passwordEncoder.encode(registroRequest.getContrasena()));
        usuario.setNombres(registroRequest.getNombres());
        usuario.setApellidos(registroRequest.getApellidos());
        usuario.setCorreo_electronico(registroRequest.getCorreo_electronico());
        usuario.setTelefono(registroRequest.getTelefono());
        usuario.setRol(rol);
        usuario.setEstado(Usuario.Estado.ACTIVO);
        usuario.setIntentos_fallidos(0);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        String accessToken = jwtService.generateAccessToken(usuarioGuardado);

        String refreshToken = jwtService.generateRefreshToken(usuarioGuardado);

        LoginResponseDTO response = new LoginResponseDTO();

        response.setAccess_token(accessToken);
        response.setRefresh_token(refreshToken);
        response.setTipo_token("Bearer");
        response.setId_usuario(usuarioGuardado.getId_usuario());
        response.setNombre_usuario(usuarioGuardado.getNombre_usuario());

        if (usuarioGuardado.getRol() != null) {
            response.setNombre_rol(usuarioGuardado.getRol().getNombre_rol());
        }

        return response;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {

        Usuario usuario = usuarioRepository
            .findByNombreUsuario(loginRequest.getNombre_usuario())
                .orElseThrow(() -> new CredencialesInvalidasException("Credenciales inválidas"));

        if (usuario.getEstado() == Usuario.Estado.BLOQUEADO) {

            throw new UsuarioBloqueadoException("El usuario se encuentra bloqueado");
        }

        if (usuario.getEstado() == Usuario.Estado.INACTIVO) {

            throw new UsuarioInactivoException("El usuario se encuentra inactivo");
        }

        boolean contrasenaCorrecta = passwordEncoder.matches(loginRequest.getContrasena(),usuario.getContrasena());

        if (!contrasenaCorrecta) {

            int intentos =
                    usuario.getIntentos_fallidos() == null
                            ? 0
                            : usuario.getIntentos_fallidos();

            intentos++;

            usuario.setIntentos_fallidos(intentos);

            if (intentos >= MAX_INTENTOS) {
                usuario.setEstado(
                        Usuario.Estado.BLOQUEADO);
            }

            usuarioRepository.save(usuario);

            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        usuario.setIntentos_fallidos(0);

        usuarioRepository.save(usuario);

        String accessToken = jwtService.generateAccessToken(usuario);

        String refreshToken =jwtService.generateRefreshToken(usuario);

        LoginResponseDTO response = new LoginResponseDTO();

        response.setAccess_token(accessToken);
        response.setRefresh_token(refreshToken);
        response.setTipo_token("Bearer");
        response.setId_usuario(usuario.getId_usuario());
        response.setNombre_usuario(usuario.getNombre_usuario());

        if (usuario.getRol() != null) {
            response.setNombre_rol(
                    usuario.getRol().getNombre_rol());
        }

        return response;
    }

    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO refreshRequest) {

        String refreshToken = refreshRequest.getRefresh_token();

        String correoElectronico;

        try {
            correoElectronico = jwtService.extractUsername(refreshToken);
        } catch (Exception e) {
            throw new IllegalArgumentException("Refresh token inválido");
        }

        Usuario usuario = usuarioRepository
            .findByCorreoElectronico(correoElectronico)
            .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no existe"));

        if (!jwtService.isRefreshTokenValid(
                refreshToken,
                usuario.getCorreo_electronico())) {

            throw new IllegalArgumentException("El refresh token es inválido o ha expirado");
        }

        if (usuario.getEstado() == Usuario.Estado.BLOQUEADO) {

            throw new IllegalArgumentException("El usuario se encuentra bloqueado");
        }

        if (usuario.getEstado() == Usuario.Estado.INACTIVO) {

            throw new IllegalArgumentException("El usuario se encuentra inactivo");
        }

        String nuevoAccessToken = jwtService.generateAccessToken(usuario);

        String nuevoRefreshToken = jwtService.generateRefreshToken(usuario);

        LoginResponseDTO response = new LoginResponseDTO();

        response.setAccess_token(nuevoAccessToken);
        response.setRefresh_token(nuevoRefreshToken);
        response.setTipo_token("Bearer");
        response.setId_usuario(usuario.getId_usuario());
        response.setNombre_usuario(usuario.getNombre_usuario());

        if (usuario.getRol() != null) {

            response.setNombre_rol(usuario.getRol().getNombre_rol());
        }

        return response;
    }
}