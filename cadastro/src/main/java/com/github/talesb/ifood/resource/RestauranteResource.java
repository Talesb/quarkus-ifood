package com.github.talesb.ifood.resource;

import java.util.List;
import java.util.Optional;

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

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.github.talesb.ifood.model.Prato;
import com.github.talesb.ifood.model.Restaurante;

@Path("/restaurantes")
@Tag(name="restaurante")
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

	@POST
	@Path("{idRestaurante}/pratos")
	@Tag(name="prato")
	@Transactional
	public Response addPratos(Prato dto, @PathParam("idRestaurante") Long idRestaurante) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Não foi encontrado nenhum restaurante com esse id");
		} else {
			Prato prato = new Prato();
			prato.nome = dto.nome;
			prato.descricao = dto.descricao;
			prato.preco = dto.preco;
			prato.restaurante = restauranteOp.get();
			prato.persist();
			return Response.status(Status.CREATED).build();
		}
	}

	@GET
	@Path("{idRestaurante}/pratos")
	@Transactional
	@Tag(name="prato")
	public List<Prato> getPratos(Prato prato, @PathParam("idRestaurante") Long idRestaurante) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Não foi encontrado nenhum restaurante com esse id");
		}
		return Prato.list("restaurante", restauranteOp.get());
	}

	@PUT
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	@Tag(name="prato")
	public void updatePratos(Prato dto, @PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Não foi encontrado nenhum restaurante com esse id");
		} else {
			Optional<Prato> pratoOp = Prato.findByIdOptional(id);

			if (pratoOp.isEmpty()) {
				throw new NotFoundException("Prato não existe");
			}

			Prato prato = pratoOp.get();
			prato.preco = dto.preco;
			prato.persist();
		}
	}

	@DELETE
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	@Tag(name="prato")
	public void deletePratos(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Não foi encontrado nenhum restaurante com esse id");
		}

		Prato.findByIdOptional(id).ifPresentOrElse(prato -> {
			prato.delete();
		}, () -> {
			throw new NotFoundException();
		});
	}

}
