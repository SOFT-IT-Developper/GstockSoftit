package bj.softit.gssft.repository.search;

import bj.softit.gssft.domain.OutStock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OutStock entity.
 */
public interface OutStockSearchRepository extends ElasticsearchRepository<OutStock, Long> {
}
