package bj.softit.gssft.repository.search;

import bj.softit.gssft.domain.Produits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Produits entity.
 */
public interface ProduitsSearchRepository extends ElasticsearchRepository<Produits, Long> {
}
