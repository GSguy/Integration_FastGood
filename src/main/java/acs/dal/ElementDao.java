package acs.dal;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import acs.data.ElementEntity;

public interface ElementDao extends PagingAndSortingRepository<ElementEntity, Long>{

    @Transactional(readOnly = true)
    List<ElementEntity> findByParentsId(Long elementId);

    @Transactional(readOnly = true)
    Page<ElementEntity> findByParentsId(Long elementId, Pageable pageable);
    
    @Transactional(readOnly = true)
    List<ElementEntity> findByChildrensId(Long elementId);

    @Transactional(readOnly = true)
    Page<ElementEntity> findByChildrensId(Long elementId, Pageable pageable);
}