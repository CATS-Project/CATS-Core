package cats.twitter.repository;

import cats.twitter.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 *
 * @author Anthony Deseille
 */

public interface ModuleRepository extends JpaRepository<Module, Integer> {
    Module findByEndpoint(String endpoint);
}
