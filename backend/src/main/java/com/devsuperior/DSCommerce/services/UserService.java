package com.devsuperior.DSCommerce.services;

import com.devsuperior.DSCommerce.DTO.RoleDTO;
import com.devsuperior.DSCommerce.DTO.UserDTO;
import com.devsuperior.DSCommerce.DTO.UserInsertDTO;
import com.devsuperior.DSCommerce.entities.Role;
import com.devsuperior.DSCommerce.entities.User;
import com.devsuperior.DSCommerce.projections.UserDetailsProjection;
import com.devsuperior.DSCommerce.repositories.RoleRepository;
import com.devsuperior.DSCommerce.repositories.UserRepository;
import com.devsuperior.DSCommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.DSCommerce.util.CustomUserUtil;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.web.authentication.PublicClientAuthenticationConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserUtil customUserUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = new User();
        user.setEmail(result.getFirst().getUsername());
        user.setPassword(result.getFirst().getPassword());
        for (UserDetailsProjection projection : result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }
        return user;
    }

    protected User authenticated() {
        try{
            String username = customUserUtil.getLoggedUsername();
            return repository.findByEmail(username);
        }
        catch (Exception e){
            throw new UsernameNotFoundException("Invalid user");
        }
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(String name, Pageable pageable) {
        Page<User> list = repository.searchByName(name, pageable);
        return list.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        User user = authenticated();
        return new UserDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = repository.findById(id);
        User user = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        try {
            User entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity (dto, entity);
        entity.getRoles().clear();
        Role role = roleRepository.findByAuthority("ROLE_CLIENT");
        entity.getRoles().add(role);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setBirthDate(dto.getBirthDate());

        entity.getRoles().clear();
        for (RoleDTO roleDTO : dto.getRoles()){
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            entity.getRoles().add(role);
        }
    }
}
