package com.hay.sweater.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hay.sweater.domain.User;
import com.hay.sweater.domain.dto.MessageDto;
  
@Repository
@Transactional
public class MessageDtoDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
  
    public Page<MessageDto> findAll(Pageable pageable, User user) {
    	
    	String sql = "select m.id, m.text, m.tag, m.user_id, m.filename, " + 
    				"count(l.user_id) likes, " +
    				"sum(case when l.user_id = ? then 1 else 0 end) > 0 meLiked " +
    				"from message m left join message_likes l on m.id = l.message_id " +
    				"group by m.id";	
    	
        Object[] args = {user.getId()};
            	 
        List<MessageDto> messageList = jdbcTemplate.query(sql, args,
                BeanPropertyRowMapper.newInstance(MessageDto.class));
        
        int start = (int) pageable.getOffset();
		int end = (start + pageable.getPageSize()) > messageList.size() ? messageList.size() : (start + pageable.getPageSize());
		
		Page<MessageDto> page = new PageImpl<MessageDto>(messageList.subList(start, end), pageable, messageList.size());
    	return page;
    }
    
    public Page<MessageDto> findByTag(Pageable pageable, String tag, User user) {
    	
    	String sql = "select m.id, m.text, m.tag, m.user_id, m.filename, " + 
    				"count(l.user_id) likes, " +
    				"sum(case when l.user_id = ? then 1 else 0 end) > 0 meLiked " +
    				"from message m left join message_likes l on m.id = l.message_id " +
    				"where m.tag = ? " +
    				"group by m.id";	
    	
        Object[] args = {user.getId(), tag};
            	 
        List<MessageDto> messageList = jdbcTemplate.query(sql, args,
                BeanPropertyRowMapper.newInstance(MessageDto.class));
        
        int start = (int) pageable.getOffset();
		int end = (start + pageable.getPageSize()) > messageList.size() ? messageList.size() : (start + pageable.getPageSize());
		
		Page<MessageDto> page = new PageImpl<MessageDto>(messageList.subList(start, end), pageable, messageList.size());
    	return page;
    }
    
    public Page<MessageDto> findByAuthor(Pageable pageable, User author, User user) {
    	String sql = "select m.id, m.text, m.tag, m.user_id, m.filename, " + 
				"count(l.user_id) likes, " +
				"sum(case when l.user_id = ? then 1 else 0 end) > 0 meLiked " +
				"from message m left join message_likes l on m.id = l.message_id " +
				"where m.user_id = ? " +
				"group by m.id";	
	
    Object[] args = {user.getId(), author.getId()};
        	 
    List<MessageDto> messageList = jdbcTemplate.query(sql, args,
            BeanPropertyRowMapper.newInstance(MessageDto.class));
    
    int start = (int) pageable.getOffset();
	int end = (start + pageable.getPageSize()) > messageList.size() ? messageList.size() : (start + pageable.getPageSize());
	
	Page<MessageDto> page = new PageImpl<MessageDto>(messageList.subList(start, end), pageable, messageList.size());
	return page;
    }
    
}