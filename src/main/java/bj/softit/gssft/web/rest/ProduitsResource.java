package bj.softit.gssft.web.rest;

import com.codahale.metrics.annotation.Timed;
import bj.softit.gssft.domain.Produits;
import bj.softit.gssft.service.ProduitsService;
import bj.softit.gssft.web.rest.util.HeaderUtil;
import bj.softit.gssft.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Produits.
 */
@RestController
@RequestMapping("/api")
public class ProduitsResource {

    private final Logger log = LoggerFactory.getLogger(ProduitsResource.class);

    private static final String ENTITY_NAME = "produits";

    private final ProduitsService produitsService;

    public ProduitsResource(ProduitsService produitsService) {
        this.produitsService = produitsService;
    }

    /**
     * POST  /produits : Create a new produits.
     *
     * @param produits the produits to create
     * @return the ResponseEntity with status 201 (Created) and with body the new produits, or with status 400 (Bad Request) if the produits has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/produits")
    @Timed
    public ResponseEntity<Produits> createProduits(@Valid @RequestBody Produits produits) throws URISyntaxException {
        log.debug("REST request to save Produits : {}", produits);
        if (produits.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new produits cannot already have an ID")).body(null);
        }
        Produits result = produitsService.save(produits);
        return ResponseEntity.created(new URI("/api/produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /produits : Updates an existing produits.
     *
     * @param produits the produits to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated produits,
     * or with status 400 (Bad Request) if the produits is not valid,
     * or with status 500 (Internal Server Error) if the produits couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/produits")
    @Timed
    public ResponseEntity<Produits> updateProduits(@Valid @RequestBody Produits produits) throws URISyntaxException {
        log.debug("REST request to update Produits : {}", produits);
        if (produits.getId() == null) {
            return createProduits(produits);
        }
        Produits result = produitsService.save(produits);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, produits.getId().toString()))
            .body(result);
    }

    /**
     * GET  /produits : get all the produits.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of produits in body
     */
    @GetMapping("/produits")
    @Timed
    public ResponseEntity<List<Produits>> getAllProduits(@ApiParam Pageable pageable, @RequestParam(required = false) String filter) {
        if ("stock-is-null".equals(filter)) {
            log.debug("REST request to get all Produitss where stock is null");
            return new ResponseEntity<>(produitsService.findAllWhereStockIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Produits");
        Page<Produits> page = produitsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/produits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /produits/:id : get the "id" produits.
     *
     * @param id the id of the produits to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the produits, or with status 404 (Not Found)
     */
    @GetMapping("/produits/{id}")
    @Timed
    public ResponseEntity<Produits> getProduits(@PathVariable Long id) {
        log.debug("REST request to get Produits : {}", id);
        Produits produits = produitsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(produits));
    }

    /**
     * DELETE  /produits/:id : delete the "id" produits.
     *
     * @param id the id of the produits to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/produits/{id}")
    @Timed
    public ResponseEntity<Void> deleteProduits(@PathVariable Long id) {
        log.debug("REST request to delete Produits : {}", id);
        produitsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/produits?query=:query : search for the produits corresponding
     * to the query.
     *
     * @param query the query of the produits search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/produits")
    @Timed
    public ResponseEntity<List<Produits>> searchProduits(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Produits for query {}", query);
        Page<Produits> page = produitsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/produits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
