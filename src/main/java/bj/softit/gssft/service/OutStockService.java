package bj.softit.gssft.service;

import bj.softit.gssft.domain.OutStock;
import bj.softit.gssft.repository.OutStockRepository;
import bj.softit.gssft.repository.search.OutStockSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OutStock.
 */
@Service
@Transactional
public class OutStockService {

    private final Logger log = LoggerFactory.getLogger(OutStockService.class);

    private final OutStockRepository outStockRepository;

    private final OutStockSearchRepository outStockSearchRepository;

    public OutStockService(OutStockRepository outStockRepository, OutStockSearchRepository outStockSearchRepository) {
        this.outStockRepository = outStockRepository;
        this.outStockSearchRepository = outStockSearchRepository;
    }

    /**
     * Save a outStock.
     *
     * @param outStock the entity to save
     * @return the persisted entity
     */
    public OutStock save(OutStock outStock) {
        log.debug("Request to save OutStock : {}", outStock);
        OutStock result = outStockRepository.save(outStock);
        outStockSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the outStocks.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OutStock> findAll() {
        log.debug("Request to get all OutStocks");
        return outStockRepository.findAll();
    }

    /**
     *  Get one outStock by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public OutStock findOne(Long id) {
        log.debug("Request to get OutStock : {}", id);
        return outStockRepository.findOne(id);
    }

    /**
     *  Delete the  outStock by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OutStock : {}", id);
        outStockRepository.delete(id);
        outStockSearchRepository.delete(id);
    }

    /**
     * Search for the outStock corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OutStock> search(String query) {
        log.debug("Request to search OutStocks for query {}", query);
        return StreamSupport
            .stream(outStockSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
