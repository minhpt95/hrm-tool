package com.vatek.hrmtool.loader;

import com.vatek.hrmtool.entity.PrivilegeEntity;
import com.vatek.hrmtool.entity.RoleEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.entity.enumeration.Privilege;
import com.vatek.hrmtool.entity.enumeration.Role;
import com.vatek.hrmtool.respository.PrivilegeRepository;
import com.vatek.hrmtool.respository.RoleRepository;
import com.vatek.hrmtool.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class MasterData implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

    public MasterData(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        PrivilegeEntity readPrivilege = createPrivilegeIfNotFound(Privilege.READ);
        PrivilegeEntity writePrivilege = createPrivilegeIfNotFound(Privilege.WRITE);
        PrivilegeEntity deletePrivilege = createPrivilegeIfNotFound(Privilege.DELETE);

        Collection<PrivilegeEntity> adminPrivileges = List.of(readPrivilege, writePrivilege,deletePrivilege);

        Collection<PrivilegeEntity> pmPrivileges = List.of(readPrivilege, writePrivilege);

        createRoleIfNotFound(Role.ADMIN, adminPrivileges);
        createRoleIfNotFound(Role.PROJECT_MANAGER,pmPrivileges);
        createRoleIfNotFound(Role.USER, List.of(readPrivilege));

        RoleEntity adminRoleEntity = roleRepository.findByRole(Role.ADMIN);

        var userEntity = userRepository.findByEmailOrIdentityCard("admin@vatek.asia","0981737983");

        if(userEntity.isEmpty()){
            UserEntity user = new UserEntity();
            user.setName("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEmail("admin@vatek.asia");
            user.setIdentityCard("0981737983");
            user.setPhoneNumber1("0981737983");
            user.setRoles(Collections.singletonList(adminRoleEntity));
            user.setEnabled(true);
            user.setCreatedBy(0L);
            user.setCreatedTime(Instant.now());
            userRepository.save(user);
        }

        alreadySetup = true;
    }

    @Transactional
    PrivilegeEntity createPrivilegeIfNotFound(Privilege pri) {

        PrivilegeEntity privilegeEntity = privilegeRepository.findByPrivilege(pri);
        if (privilegeEntity == null) {
            privilegeEntity = new PrivilegeEntity();
            privilegeEntity.setPrivilege(pri);
            privilegeEntity.setCreatedBy(0L);
            privilegeEntity.setCreatedTime(Instant.now());
            privilegeRepository.save(privilegeEntity);
        }
        return privilegeEntity;
    }

    @Transactional
    RoleEntity createRoleIfNotFound(Role role, Collection<PrivilegeEntity> privileges) {

        RoleEntity roleEntity = roleRepository.findByRole(role);
        if (roleEntity == null) {
            roleEntity = new RoleEntity();
            roleEntity.setRole(role);
            roleEntity.setPrivileges(privileges);
            roleEntity.setCreatedBy(0L);
            roleEntity.setCreatedTime(Instant.now());
            roleRepository.save(roleEntity);
        }
        return roleEntity;
    }
}
