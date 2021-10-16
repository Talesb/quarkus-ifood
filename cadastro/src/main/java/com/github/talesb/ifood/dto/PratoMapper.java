package com.github.talesb.ifood.dto;

import org.mapstruct.Mapper;

import com.github.talesb.ifood.model.Prato;

@Mapper(componentModel = "cdi")
public interface PratoMapper {

	public Prato toPrato(AdicionarPratoDTO dto);

	public AdicionarPratoDTO toAdicionarPratoDTO(Prato prato);

}
