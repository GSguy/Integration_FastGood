package acs.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import acs.data.ElementEntity;

public interface ElementDao extends PagingAndSortingRepository<ElementEntity, Long>{

  /* @Transactional(readOnly = true)
    List<ElementEntity> findByParentsId(@Param("elementId")Long elementId,Pageable pageable);

   /* @Transactional(readOnly = true)
    Page<ElementEntity> findByParentsId(@Param("elementId")Long elementId, Pageable pageable);*/
    
    /*@Transactional(readOnly = true)
    List<ElementEntity> findByChildrensId(@Param("elementId")Long elementId,Pageable pageable);

    /*@Transactional(readOnly = true)
    Page<ElementEntity> findByChildrensId(@Param("elementId")Long elementId, Pageable pageable); */
	
	public List<ElementEntity> findAllByActive(@Param("active")Boolean active);
	
	public ElementEntity findOneByElementIdAndActive(@Param("elementId")Long elementId,@Param("active")Boolean active);
}