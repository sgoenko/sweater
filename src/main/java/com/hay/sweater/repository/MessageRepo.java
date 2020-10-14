package com.hay.sweater.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hay.sweater.domain.Message;
import com.hay.sweater.domain.User;
import com.hay.sweater.domain.dto.MessageDto;

public interface MessageRepo extends CrudRepository<Message, Long> {
	
	@Query("select new com.hay.sweater.domain.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "group by m")
	Page<MessageDto> findAll(Pageable pageable, @Param("user") User user);
	
	@Query("select new com.hay.sweater.domain.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.tag = :tag " +
            "group by m")
    Page<MessageDto> findByTag(Pageable pageable, @Param("tag") String tag, @Param("user") User user);

    @Query("select new com.hay.sweater.domain.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.author = :author " +
            "group by m")
    Page<MessageDto> findByAuthor(Pageable pageable, @Param("author") User author, @Param("user") User user);

}
