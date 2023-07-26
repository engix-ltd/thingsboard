/**
 * Copyright © 2016-2023 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.dao.sql.tenant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.thingsboard.server.dao.model.sql.TenantEntity;
import org.thingsboard.server.dao.model.sql.TenantInfoEntity;

import java.util.List;
import java.util.UUID;

/**
 * Created by Valerii Sosliuk on 4/30/2017.
 */
public interface TenantRepository extends JpaRepository<TenantEntity, UUID> {

    @Query("SELECT new org.thingsboard.server.dao.model.sql.TenantInfoEntity(t, p.name) " +
            "FROM TenantEntity t " +
            "LEFT JOIN TenantProfileEntity p on p.id = t.tenantProfileId " +
            "WHERE t.id = :tenantId")
    TenantInfoEntity findTenantInfoById(@Param("tenantId") UUID tenantId);

    @Query("SELECT t FROM TenantEntity t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :textSearch, '%'))")
    Page<TenantEntity> findTenantsNextPage(@Param("textSearch") String textSearch,
                                           Pageable pageable);

    @Query("SELECT new org.thingsboard.server.dao.model.sql.TenantInfoEntity(t, p.name) " +
            "FROM TenantEntity t " +
            "LEFT JOIN TenantProfileEntity p on p.id = t.tenantProfileId " +
            "WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :textSearch, '%'))")
    Page<TenantInfoEntity> findTenantInfosNextPage(@Param("textSearch") String textSearch,
                                                          Pageable pageable);

    @Query("SELECT t.id FROM TenantEntity t")
    Page<UUID> findTenantsIds(Pageable pageable);

    @Query("SELECT t.id FROM TenantEntity t where t.tenantProfileId = :tenantProfileId")
    List<UUID> findTenantIdsByTenantProfileId(@Param("tenantProfileId") UUID tenantProfileId);
}
