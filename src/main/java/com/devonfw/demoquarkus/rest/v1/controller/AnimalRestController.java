package com.devonfw.demoquarkus.rest.v1.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.tkit.quarkus.rs.models.PageResultDTO;

import com.devonfw.demoquarkus.domain.model.Animal;
import com.devonfw.demoquarkus.domain.repo.AnimalRepository;
import com.devonfw.demoquarkus.rest.v1.mapper.AnimalMapper;
import com.devonfw.demoquarkus.rest.v1.model.AnimalDto;
import com.devonfw.demoquarkus.rest.v1.model.AnimalSearchCriteriaDto;
import com.devonfw.demoquarkus.rest.v1.model.NewAnimalDto;

import lombok.extern.slf4j.Slf4j;

//In Quarkus all JAX-RS resources are treated as CDI beans
//default is Singleton scope
@Path("/animals")
// how we serialize response
@Produces(MediaType.APPLICATION_JSON)
// how we deserialize params
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class AnimalRestController {

  // our class is Bean(implicit Appscope), so we can inject any CDI bean into it
  @Inject
  AnimalRepository animalRepository;

  // mapstruct-generated mappers are CDI beans, we can inject them, see pom.xml#161
  @Inject
  AnimalMapper mapper;

  // using @Context we can inject contextual info from JAXRS(e.g. http request, current uri info, endpoint info...)
  @Context
  UriInfo uriInfo;

  @APIResponses({
  @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PagedAnimalResponse.class))),
  @APIResponse(responseCode = "500") })
  @Operation(operationId = "Get Animals", description = "Returns list of animals matching given criteria, uses pagination")
  @GET
  // REST service methods should not declare exceptions, any thrown error will be transformed by exceptionMapper in
  // tkit-rest
  // We did not define custom @Path - so it will use class level path
  public PageImpl<AnimalDto> getAll(@BeanParam AnimalSearchCriteriaDto dto) {

    Iterable<Animal> animalsIterator = this.animalRepository.findAll();
    List<Animal> animals = new ArrayList<Animal>();
    animalsIterator.forEach(animals::add);
    List<AnimalDto> animalsDto = this.mapper.map(animals);
    return new PageImpl<>(animalsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animalsDto.size());
  }

  @GET
  @Path("criteriaApi")
  public PageImpl<AnimalDto> getAllCriteriaApi(@BeanParam AnimalSearchCriteriaDto dto) {

    List<Animal> animals = this.animalRepository.findAllCriteriaApi(dto).getContent();
    List<AnimalDto> animalsDto = this.mapper.map(animals);
    return new PageImpl<>(animalsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animalsDto.size());
  }

  @GET
  @Path("queryDsl")
  public PageImpl<AnimalDto> getAllQueryDsl(@BeanParam AnimalSearchCriteriaDto dto) {

    List<Animal> animals = this.animalRepository.findAllQueryDsl(dto).getContent();
    List<AnimalDto> animalsDto = this.mapper.map(animals);
    return new PageImpl<>(animalsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animalsDto.size());
  }

  @GET
  @Path("query")
  public PageImpl<AnimalDto> getAllQuery(@BeanParam AnimalSearchCriteriaDto dto) {

    List<Animal> animals = this.animalRepository.findByNameQuery(dto).getContent();
    List<AnimalDto> animalsDto = this.mapper.map(animals);
    return new PageImpl<>(animalsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animalsDto.size());
  }

  @GET
  @Path("nativeQuery")
  public PageImpl<AnimalDto> getAllNativeQuery(@BeanParam AnimalSearchCriteriaDto dto) {

    List<Animal> animals = this.animalRepository.findByNameNativeQuery(dto).getContent();
    List<AnimalDto> animalsDto = this.mapper.map(animals);
    return new PageImpl<>(animalsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animalsDto.size());
  }

  @GET
  @Path("ordered")
  public PageImpl<AnimalDto> getAllOrderedByName() {

    List<Animal> animals = this.animalRepository.findAllByOrderByName().getContent();
    List<AnimalDto> animalsDto = this.mapper.map(animals);
    return new PageImpl<>(animalsDto);
  }

  @APIResponses({
  @APIResponse(responseCode = "201", description = "OK, New animal created", content = @Content(schema = @Schema(implementation = NewAnimalDto.class))),
  @APIResponse(responseCode = "400", description = "Client side error, invalid request"),
  @APIResponse(responseCode = "500") })
  @Operation(operationId = "createNewAnimal", description = "Stores new animal in DB")
  @POST
  // We did not define custom @Path - so it will use class level path.
  // Although we now have 2 methods with same path, it is ok, because it is a different method (get vs post)
  public AnimalDto createNewAnimal(NewAnimalDto dto) {

    Animal created = this.animalRepository.save(this.mapper.create(dto));
    return this.mapper.map(created);
  }

  @APIResponses({
  @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AnimalDto.class))),
  @APIResponse(responseCode = "404", description = "Animal not found"), @APIResponse(responseCode = "500") })
  @Operation(operationId = "getAnimalById", description = "Returns animal with given id")
  @GET
  @Path("{id}")
  public AnimalDto getAnimalById(@Parameter(description = "Animal unique id") @PathParam("id") String id) {

    Animal animal = this.animalRepository.findById(Long.valueOf(id)).get();
    if (animal != null) {
      return this.mapper.map(animal);
    } else {
      return null;
    }
  }

  @GET
  @Path("name/{name}")
  public AnimalDto getAnimalByName(@PathParam("name") String name) {

    Animal animal = this.animalRepository.findByName(name);
    if (animal != null) {
      return this.mapper.map(animal);
    } else {
      return null;
    }
  }

  @APIResponses({
  @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AnimalDto.class))),
  @APIResponse(responseCode = "404", description = "Animal not found"), @APIResponse(responseCode = "500") })
  @Operation(operationId = "deleteAnimalById", description = "Deletes the animal with given id")
  @DELETE
  @Path("{id}")
  // we add transaction here, cause we do select and then pass the detached entity to DAO for deletion
  @Transactional
  public AnimalDto deleteAnimalByName(@Parameter(description = "Animal unique id") @PathParam("id") String id) {

    Animal animal = this.animalRepository.findById(Long.valueOf(id)).get();
    if (animal != null) {
      this.animalRepository.delete(animal);
      return this.mapper.map(animal);
    } else {
      return null;
    }
  }

  // here we simulate calling a source, that can fail
  @GET
  @Path("{id}/facts")
  // we will retry upto 4 times if this method throws given exception
  // we also delay for 300ms
  @Retry(maxRetries = 4, retryOn = IllegalStateException.class, delay = 300)
  public List<String> getAnimalFacts(@PathParam("id") String id) {

    return getFactsFromUnreliableSource(id);
  }

  private List<String> getFactsFromUnreliableSource(String id) {

    // our source will randomly fail
    boolean willFail = new Random().nextBoolean();
    if (willFail) {
      log.info("Ooops, fact fetching failed");
      throw new IllegalStateException("Unreliable source failed");
    } else {
      log.info("Cool, fact fetching succeed");
      return List.of("imagine real data here", "and also this shocking fact");
    }
  }

  private static class PagedAnimalResponse extends PageResultDTO<AnimalDto> {
  }

}
