package com.estudosjonath.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estudosjonath.dscatalog.dto.CategoryDTO;
import com.estudosjonath.dscatalog.dto.ProductDTO;
import com.estudosjonath.dscatalog.entities.Category;
import com.estudosjonath.dscatalog.entities.Product;
import com.estudosjonath.dscatalog.repositories.CategoryRepository;
import com.estudosjonath.dscatalog.repositories.ProductRepository;
import com.estudosjonath.dscatalog.services.exceptions.DataBaseException;
import com.estudosjonath.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = repository.findAll(pageable);

		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not Found"));

		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();

		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);

		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (javax.persistence.EntityNotFoundException e) {
			throw new EntityNotFoundException("Id not found" + id);

		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("Id not found" + " " + id + "Entrei aqui");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integreti Violation");
		}
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());

		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {
			@SuppressWarnings("deprecation")
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}

	}

}
