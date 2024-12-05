package ai.monitorize.chatbot4j.common;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RedisRepository<T, ID> extends CrudRepository<T, ID> {
}
