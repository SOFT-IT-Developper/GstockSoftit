package bj.softit.gssft.service;

import bj.softit.gssft.domain.Historiques;
import bj.softit.gssft.repository.HistoriquesRepository;
import bj.softit.gssft.repository.search.HistoriquesSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Historiques.
 */
@Service
@Transactional
public class HistoriquesService {

    private final Logger log = LoggerFactory.getLogger(HistoriquesService.class);

    private final HistoriquesRepository historiquesRepository;

    private final HistoriquesSearchRepository historiquesSearchRepository;

    public HistoriquesService(HistoriquesRepository historiquesRepository, HistoriquesSearchRepository historiquesSearchRepository) {
        this.historiquesRepository = historiquesRepository;
        this.historiquesSearchRepository = historiquesSearchRepository;
    }

    /**
     * Save a historiques.
     *
     * @param historiques the entity to save
     * @return the persisted entity
     */
    public Historiques save(Historiques historiques) {
        log.debug("Request to save Historiques : {}", historiques);
        Historiques result = historiquesRepository.save(historiques);
        historiquesSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the historiques.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Historiques> findAll() {
        log.debug("Request to get all Historiques");
        return historiquesRepository.findAll();
    }

    /**
     *  Get one historiques by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Historiques findOne(Long id) {
        log.debug("Request to get Historiques : {}", id);
        return historiquesRepository.findOne(id);
    }

    /**
     *  Delete the  historiques by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Historiques : {}", id);
        historiquesRepository.delete(id);
        historiquesSearchRepository.delete(id);
    }

    /**
     * Search for the historiques corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Historiques> search(String query) {
        log.debug("Request to search Historiques for query {}", query);
        return StreamSupport
            .stream(historiquesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
