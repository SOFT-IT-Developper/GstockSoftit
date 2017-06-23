package bj.softit.gssft.service;

import bj.softit.gssft.domain.Historiques;
import bj.softit.gssft.domain.OutStock;
import bj.softit.gssft.domain.Produits;
import bj.softit.gssft.domain.Stock;
import bj.softit.gssft.repository.HistoriquesRepository;
import bj.softit.gssft.repository.ProduitsRepository;
import bj.softit.gssft.repository.search.HistoriquesSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;
import org.springframework.data.domain.Sort;
/**
 * Service Implementation for managing Historiques.
 */
@Service
@Transactional
public class HistoriquesService {

    private final Logger log = LoggerFactory.getLogger(HistoriquesService.class);

    private final HistoriquesRepository historiquesRepository;
    private final ProduitsRepository produitsRepository;
    private final HistoriquesSearchRepository historiquesSearchRepository;
    private final UserService userService;

    public HistoriquesService(HistoriquesRepository historiquesRepository, ProduitsRepository produitsRepository, HistoriquesSearchRepository historiquesSearchRepository, UserService userService) {
        this.historiquesRepository = historiquesRepository;
        this.produitsRepository = produitsRepository;
        this.historiquesSearchRepository = historiquesSearchRepository;
        this.userService = userService;
    }

    /**
     * Save a historiques.
     *
     * @param historiques the entity to save
     * @return the persisted entity
     */
    public Historiques save(Historiques historiques) {
        log.debug("Request to save Historiques : {}", historiques);
        Historiques result = historiquesRepository.save(historiques.user(userService.getUserWithAuthorities()));
        historiquesSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the historiques.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Historiques> findAll() {
        log.debug("Request to get all Historiques");
        return historiquesRepository.findAll(sortByIdAsc());
    }
    private Sort sortByIdAsc() {
        return new Sort(Sort.Direction.ASC, "operation");
    }
    /**
     * Get one historiques by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Historiques findOne(Long id) {
        log.debug("Request to get Historiques : {}", id);
        return historiquesRepository.findOne(id);
    }


    /**
     * Delete the  historiques by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Historiques : {}", id);
        historiquesRepository.delete(id);
        historiquesSearchRepository.delete(id);
    }

    /**
     * Search for the historiques corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Historiques> search(String query) {
        log.debug("Request to search Historiques for query {}", query);
        return StreamSupport
            .stream(historiquesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public void addHistOut(OutStock outStock) {

        historiquesRepository.save(new Historiques().operation("Sortir de stock")
            .date(ZonedDateTime.now()).user(userService.getUserWithAuthorities()));
        Produits produits = produitsRepository.findOne(outStock.getProduit().getId());
        //BigDecimal a= outStock.getQuantite().negate();
        produits.getStock().setQuantite(produits.getStock().getQuantite().add(outStock.getQuantite().negate()));
    }

    public void addHistEnter(Stock stock) {
        historiquesRepository.save(new Historiques()
            .date(ZonedDateTime.now())
            .operation("Entrer de stock")
            .user(userService.getUserWithAuthorities()));
        Produits produits = produitsRepository.findOne(stock.getProduit().getId());
        //BigDecimal a= outStock.getQuantite().negate();
       // produits.getStock().setQuantite(produits.getStock().getQuantite().add(stock.getQuantite()));
    }

    public void addHist(String s) {
        historiquesRepository.save(new Historiques().date(ZonedDateTime.now()).operation(s).user(userService.getUserWithAuthorities()));
    }
}
