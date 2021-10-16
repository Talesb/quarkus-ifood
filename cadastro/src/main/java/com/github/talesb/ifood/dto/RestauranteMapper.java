package com.github.talesb.ifood.dto;

import org.mapstruct.Mapper;

import com.github.talesb.ifood.model.Restaurante;

@Mapper(componentModel="cdi")
public interface RestauranteMapper {

	public Restaurante toRestaurante(AdicionarRestauranteDTO dto);
	
	public AdicionarRestauranteDTO toAdicionarRestauranteDTO(Restaurante restaurante);
	
}
