package bj.softit.gssft.web.rest;

import bj.softit.gssft.service.HistoriquesService;
import com.codahale.metrics.annotation.Timed;
import bj.softit.gssft.domain.OutStock;
import bj.softit.gssft.service.OutStockService;
import bj.softit.gssft.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OutStock.
 */
@RestController
@RequestMapping("/api")
public class OutStockResource {

    private final Logger log = LoggerFactory.getLogger(OutStockResource.class);

    private static final String ENTITY_NAME = "outStock";

    private final OutStockService outStockService;
    private final HistoriquesService historiquesService;

    public OutStockResource(OutStockService outStockService, HistoriquesService historiquesService) {
        this.outStockService = outStockService;
        this.historiquesService = historiquesService;
    }

    /**
     * POST  /out-stocks : Create a new outStock.
     *
     * @param outStock the outStock to create
     * @return the ResponseEntity with status 201 (Created) and with body the new outStock, or with status 400 (Bad Request) if the outStock has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/out-stocks")
    @Timed
    public ResponseEntity<OutStock> createOutStock(@Valid @RequestBody OutStock outStock) throws URISyntaxException {
        log.debug("REST request to save OutStock : {}", outStock);
        if (outStock.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new outStock cannot already have an ID")).body(null);
        }
        historiquesService.addHistOut(outStock);
        OutStock result = outStockService.save(outStock);
        return ResponseEntity.created(new URI("/api/out-stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /out-stocks : Updates an existing outStock.
     *
     * @param outStock the outStock to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated outStock,
     * or with status 400 (Bad Request) if the outStock is not valid,
     * or with status 500 (Internal Server Error) if the outStock couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/out-stocks")
    @Timed
    public ResponseEntity<OutStock> updateOutStock(@Valid @RequestBody OutStock outStock) throws URISyntaxException {
        log.debug("REST request to update OutStock : {}", outStock);
        if (outStock.getId() == null) {
            return createOutStock(outStock);
        }
        OutStock result = outStockService.save(outStock);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, outStock.getId().toString()))
            .body(result);
    }

    /**
     * GET  /out-stocks : get all the outStocks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of outStocks in body
     */
    @GetMapping("/out-stocks")
    @Timed
    public List<OutStock> getAllOutStocks() {
        log.debug("REST request to get all OutStocks");
        return outStockService.findAll();
    }

    /**
     * GET  /out-stocks/:id : get the "id" outStock.
     *
     * @param id the id of the outStock to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the outStock, or with status 404 (Not Found)
     */
    @GetMapping("/out-stocks/{id}")
    @Timed
    public ResponseEntity<OutStock> getOutStock(@PathVariable Long id) {
        log.debug("REST request to get OutStock : {}", id);
        OutStock outStock = outStockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(outStock));
    }

    /**
     * DELETE  /out-stocks/:id : delete the "id" outStock.
     *
     * @param id the id of the outStock to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/out-stocks/{id}")
    @Timed
    public ResponseEntity<Void> deleteOutStock(@PathVariable Long id) {
        log.debug("REST request to delete OutStock : {}", id);
        outStockService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/out-stocks?query=:query : search for the outStock corresponding
     * to the query.
     *
     * @param query the query of the outStock search
     * @return the result of the search
     */
    @GetMapping("/_search/out-stocks")
    @Timed
    public List<OutStock> searchOutStocks(@RequestParam String query) {
        log.debug("REST request to search OutStocks for query {}", query);
        return outStockService.search(query);
    }

}
