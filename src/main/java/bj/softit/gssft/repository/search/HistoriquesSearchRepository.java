package bj.softit.gssft.repository.search;

import bj.softit.gssft.domain.Historiques;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Historiques entity.
 */
public interface HistoriquesSearchRepository extends ElasticsearchRepository<Historiques, Long> {
}
