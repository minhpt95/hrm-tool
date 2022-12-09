package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.MasterDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MasterDataRepository extends JpaRepository<MasterDataEntity,Long>, JpaSpecificationExecutor<MasterDataEntity> {
    MasterDataEntity findMasterDataEntityByTypeAndCodeAndNameAndActive(
            String type,
            String code,
            String name,
            boolean active
    );
}
