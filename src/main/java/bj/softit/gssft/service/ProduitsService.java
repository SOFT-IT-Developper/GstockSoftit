package bj.softit.gssft.service;

import bj.softit.gssft.domain.Produits;
import bj.softit.gssft.repository.ProduitsRepository;
import bj.softit.gssft.repository.search.ProduitsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Produits.
 */
@Service
@Transactional
public class ProduitsService {

    private final Logger log = LoggerFactory.getLogger(ProduitsService.class);

    private final ProduitsRepository produitsRepository;

    private final ProduitsSearchRepository produitsSearchRepository;

    public ProduitsService(ProduitsRepository produitsRepository, ProduitsSearchRepository produitsSearchRepository) {
        this.produitsRepository = produitsRepository;
        this.produitsSearchRepository = produitsSearchRepository;
    }

    /**
     * Save a produits.
     *
     * @param produits the entity to save
     * @return the persisted entity
     */
    public Produits save(Produits produits) {
        log.debug("Request to save Produits : {}", produits);
        Produits result = produitsRepository.save(produits);
        produitsSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the produits.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Produits> findAll(Pageable pageable) {
        log.debug("Request to get all Produits");
        return produitsRepository.findAll(pageable);
    }


    /**
     *  get all the produits where Stock is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Produits> findAllWhereStockIsNull() {
        log.debug("Request to get all produits where Stock is null");
        return StreamSupport
            .stream(produitsRepository.findAll().spliterator(), false)
            .filter(produits -> produits.getStock() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one produits by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Produits findOne(Long id) {
        log.debug("Request to get Produits : {}", id);
        return produitsRepository.findOne(id);
    }

    /**
     *  Delete the  produits by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Produits : {}", id);
        produitsRepository.delete(id);
        produitsSearchRepository.delete(id);
    }

    /**
     * Search for the produits corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Produits> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Produits for query {}", query);
        Page<Produits> result = produitsSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
