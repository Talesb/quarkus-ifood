package com.github.talesb.ifood.resource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
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

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.github.talesb.ifood.dto.AdicionarPratoDTO;
import com.github.talesb.ifood.dto.AdicionarRestauranteDTO;
import com.github.talesb.ifood.dto.PratoMapper;
import com.github.talesb.ifood.dto.RestauranteMapper;
import com.github.talesb.ifood.infra.ConstraintViolationResponse;
import com.github.talesb.ifood.model.Prato;
import com.github.talesb.ifood.model.Restaurante;

@Path("/restaurantes")
@Tag(name = "restaurante")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("proprietario")
@SecurityScheme(securitySchemeName = "ifood-oauth", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(password = @OAuthFlow(tokenUrl = "http://localhost:8180/auth/realms/ifood/protocol/openid-connect/token")))
@SecurityRequirement(name = "ifood-oauth",scopes = "{}")
public class RestauranteResource {

	@Inject
	RestauranteMapper restauranteMapper;

	@Inject
	PratoMapper pratoMapper;

	@GET
	public List<AdicionarRestauranteDTO> getAll() {
		return Restaurante.streamAll()
				.map(restaurante -> restauranteMapper.toAdicionarRestauranteDTO((Restaurante) restaurante))
				.collect(Collectors.toList());
	}

	@POST
	@Transactional
	@APIResponse(responseCode = "201", description = "Retorno com sucesso")
	@APIResponse(responseCode = "400", content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class)))
	public void addRestaurantes(@Valid AdicionarRestauranteDTO dto) {
		Restaurante restaurante = restauranteMapper.toRestaurante(dto);
		restaurante.persist();
		Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public void updateRestaurantes(@PathParam("id") Long id, AdicionarRestauranteDTO dto) {
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
	@Tag(name = "prato")
	@Transactional
	public Response addPratos(AdicionarPratoDTO dto, @PathParam("idRestaurante") Long idRestaurante) {
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
	@Tag(name = "prato")
	public List<AdicionarPratoDTO> getPratos(Prato prato, @PathParam("idRestaurante") Long idRestaurante) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Não foi encontrado nenhum restaurante com esse id");
		}

		List<Prato> pratos = Prato.list("restaurante", restauranteOp.get());
		return pratos.stream().map(pratoParam -> pratoMapper.toAdicionarPratoDTO(pratoParam))
				.collect(Collectors.toList());
	}

	@PUT
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	@Tag(name = "prato")
	public void updatePratos(AdicionarPratoDTO dto, @PathParam("idRestaurante") Long idRestaurante,
			@PathParam("id") Long id) {
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
	@Tag(name = "prato")
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
