package acs.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import acs.data.ElementEntity;

public interface ElementDao extends PagingAndSortingRepository<ElementEntity, Long>{

	@Transactional(readOnly = true)
   	public Page<ElementEntity> findByParentsElementId(@Param("elementId")Long elementId, Pageable pageable);

   @Transactional(readOnly = true)
   	public Page<ElementEntity> findByParentsElementIdAndActive(@Param("elementId")Long elementId, @Param("active")Boolean active,  Pageable pageable);
	
   @Transactional(readOnly = true)
   public Page<ElementEntity> findByChildrensElementId(@Param("elementId")Long elementId, Pageable pageable); 
	
   @Transactional(readOnly = true)
	public Page<ElementEntity> findByChildrensElementIdAndActive(@Param("elementId")Long elementId, @Param("active")Boolean active , Pageable pageable); 
   
	
	public List<ElementEntity> findAllByActive(@Param("active")Boolean active);
	
	public Page<ElementEntity> findAllByActive(@Param("active")Boolean active, Pageable pageable);
	
	public ElementEntity findOneByElementIdAndActive(@Param("elementId")Long elementId,@Param("active")Boolean active);
	
	
	public Page<ElementEntity>  findByName(@Param("name")String name, Pageable pageable);
	
	public Page<ElementEntity>  findByNameAndActive(@Param("name")String name, @Param("active")Boolean active, Pageable pageable);
	
	public Page<ElementEntity>  findByType(@Param("type")String type, Pageable pageable);
	
	public Page<ElementEntity>  findByTypeAndActive(@Param("type")String type,@Param("active")Boolean active, Pageable pageable);
}