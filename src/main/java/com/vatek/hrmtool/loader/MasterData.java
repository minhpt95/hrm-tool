package com.vatek.hrmtool.loader;

import com.vatek.hrmtool.entity.MasterDataEntity;
import com.vatek.hrmtool.entity.PrivilegeEntity;
import com.vatek.hrmtool.entity.RoleEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.entity.enumeration.Privilege;
import com.vatek.hrmtool.entity.enumeration.Role;
import com.vatek.hrmtool.respository.MasterDataRepository;
import com.vatek.hrmtool.respository.PrivilegeRepository;
import com.vatek.hrmtool.respository.RoleRepository;
import com.vatek.hrmtool.respository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class MasterData implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final MasterDataRepository masterDataRepository;

    private final PasswordEncoder passwordEncoder;

    public MasterData(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, MasterDataRepository masterDataRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.masterDataRepository = masterDataRepository;
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

        List<MasterDataEntity> masterDataEntities = List.of(
                new MasterDataEntity("POSITION","CENTRAL_EXECUTIVE_OFFICER","Central Executive Officer",true),
                new MasterDataEntity("POSITION","DIRECTOR_MANAGEMENT","Director Management",true),
                new MasterDataEntity("POSITION","PRODUCT_MANAGER","Project Manager",true),
                new MasterDataEntity("POSITION","PRODUCT_OWNER","Project Manager",true),
                new MasterDataEntity("POSITION","PROJECT_MANAGER","Project Manager",true),
                new MasterDataEntity("POSITION","DEVELOPER","Developer",true),
                new MasterDataEntity("POSITION","TESTER","Tester",true),
                new MasterDataEntity("POSITION","IT_ADMIN","Central Executive Officer",true),
                new MasterDataEntity("POSITION","BUSINESS_ANALYST","Central Executive Officer",true),
                new MasterDataEntity("POSITION","QUALITY_ASSURANCE","Central Executive Officer",true),
                new MasterDataEntity("POSITION","QUALITY_CONTROL","Central Executive Officer",true),
                new MasterDataEntity("POSITION","DEVOPS","DevOps",true),
                new MasterDataEntity("POSITION","Data_Engineer","Central Executive Officer",true),
                new MasterDataEntity("POSITION","Data_Scientist","Central Executive Officer",true),
                new MasterDataEntity("POSITION","Scrum_Master","Central Executive Officer",true),
                new MasterDataEntity("POSITION","Agile Coach","Central Executive Officer",true),
                new MasterDataEntity("PROGRAM_LANGUAGE","JAVA","Java",true),
                new MasterDataEntity("LEVEL","INTERN","Intern",true),
                new MasterDataEntity("LEVEL","FRESHER","Fresher",true),
                new MasterDataEntity("LEVEL","JUNIOR","Junior",true),
                new MasterDataEntity("LEVEL","MIDDLE","Middle",true),
                new MasterDataEntity("LEVEL","SENIOR","Senior",true)
        );

        masterDataEntities.forEach(this::createMasterDataIfNotFound);

        alreadySetup = true;
    }

    @Transactional
    PrivilegeEntity createPrivilegeIfNotFound(Privilege pri) {

        PrivilegeEntity privilegeEntity = privilegeRepository.findByPrivilege(pri);
        if (privilegeEntity != null) {
            return privilegeEntity;
        }

        privilegeEntity = new PrivilegeEntity();
        privilegeEntity.setPrivilege(pri);
        privilegeEntity.setCreatedBy(0L);
        privilegeEntity.setCreatedTime(Instant.now());
        privilegeRepository.save(privilegeEntity);

        return privilegeEntity;
    }

    @Transactional
    void createMasterDataIfNotFound(MasterDataEntity masterDataEntityCreate){
        MasterDataEntity masterDataEntity = masterDataRepository.findMasterDataEntityByTypeAndCodeAndNameAndActive(
                masterDataEntityCreate.getType(),
                masterDataEntityCreate.getCode(),
                masterDataEntityCreate.getName(),
                true
        );

        if(masterDataEntity != null){
            return;
        }

        masterDataEntityCreate.setCreatedBy(0L);
        masterDataEntityCreate.setCreatedTime(Instant.now());

        masterDataRepository.save(masterDataEntityCreate);
    }

    @Transactional
    void createRoleIfNotFound(Role role, Collection<PrivilegeEntity> privileges) {

        RoleEntity roleEntity = roleRepository.findByRole(role);
        if (roleEntity != null) {
            return;
        }

        roleEntity = new RoleEntity();
        roleEntity.setRole(role);
        roleEntity.setPrivileges(privileges);
        roleEntity.setCreatedBy(0L);
        roleEntity.setCreatedTime(Instant.now());

        roleRepository.save(roleEntity);
    }
}
