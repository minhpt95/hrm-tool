package com.vatek.hrmtool.security.service;


import com.vatek.hrmtool.entity.PrivilegeEntity;
import com.vatek.hrmtool.entity.RoleEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.enumeration.Privilege;
import com.vatek.hrmtool.enumeration.Role;
import com.vatek.hrmtool.respository.RoleRepository;
import com.vatek.hrmtool.respository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    final
    private UserRepository userRepository;

    final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserPrinciple loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with -> username or email : " + username)
                );

        Collection<RoleEntity> roleEntities = userEntity.getRoles();

        var rolesAuthority = roleEntities
                .stream()
                .map(RoleEntity::getRole)
                .map(Role::getAuthority)
                .toList();

        var privilegesAuthority = roleEntities
                .stream()
                .map(RoleEntity::getPrivileges)
                .flatMap(Collection::stream)
                .map(PrivilegeEntity::getPrivilege)
                .map(Privilege::getAuthority)
                .toList();

        return UserPrinciple
                .userPrincipleBuilder(userEntity)
                .authorities(getAuthorities(roleEntities))
                .roles(rolesAuthority)
                .privileges(privilegesAuthority)
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<RoleEntity> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }
    private List<String> getPrivileges(Collection<RoleEntity> roles) {

        List<String> privileges = new ArrayList<>();
        List<PrivilegeEntity> collection = new ArrayList<>();
        for (RoleEntity role : roles) {
            privileges.add(role.getRole().getAuthority());
            collection.addAll(role.getPrivileges());
        }
        for (PrivilegeEntity item : collection) {
            privileges.add(item.getPrivilege().getAuthority());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
