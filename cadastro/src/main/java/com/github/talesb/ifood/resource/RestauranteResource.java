package com.github.talesb.ifood.resource;

import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.github.talesb.ifood.model.Restaurante;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

	@GET
	public List<Restaurante> getAll() {
		return Restaurante.listAll();
	}

	@POST
	@Transactional
	public void addRestaurantes(Restaurante dto) {
		dto.persist();
		Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public void updateRestaurantes(@PathParam("id") Long id, Restaurante dto) {
		Restaurante.findByIdOptional(id).map(restaurante -> {
			((Restaurante) restaurante).nome = dto.nome;
			restaurante.persist();
			return restaurante;
		}).orElseGet(() -> {
			throw new NotFoundException();
		});

	}

	@DELETE
	@Path("{id}")
	@Transactional
	public void deleteRestaurantes(@PathParam("id") Long id) {
		Restaurante.findByIdOptional(id).ifPresentOrElse(restaurante -> {
			restaurante.delete();
		}, () -> {
			throw new NotFoundException();
		});
	}

}
