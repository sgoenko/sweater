package com.hay.sweater.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hay.sweater.domain.User;
import com.hay.sweater.domain.dto.MessageDto;
import com.hay.sweater.repository.MessageRepo;

@Service
public class MessageService {
	
	@Autowired
	private MessageRepo messageRepo;
	
//	@Autowired
//	private MessageDtoDao messageDtoDao;
	
	public Page<MessageDto> messageList(Pageable pageable, String filter, User user) {
		if (filter != null && !filter.isEmpty()) {
			return messageRepo.findByTag(pageable, filter, user);
//			return messageDtoDao.findByTag(pageable, filter, user);
		} else {
			return messageRepo.findAll(pageable, user);
//			return messageDtoDao.findAll(pageable, user);
		}
	}

	public Page<MessageDto> messageListForUser(Pageable pageable, User currentUser, User author) {
        return messageRepo.findByAuthor(pageable, author, currentUser);
//        return messageDtoDao.findByAuthor(pageable, author, currentUser);
    }

}
