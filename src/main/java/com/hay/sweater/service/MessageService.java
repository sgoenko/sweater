package com.hay.sweater.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hay.sweater.domain.Message;
import com.hay.sweater.domain.User;
import com.hay.sweater.domain.dto.MessageDto;
import com.hay.sweater.repository.MessageRepo;

@Service
public class MessageService {
	
	@Autowired
	private MessageRepo messageRepo;
	
//	@Autowired
//	private MessageDtoDao messageDtoDao;
	
	public Page<MessageDto> messageList(Pageable pageable, String tag, User user) {
		if (tag != null && !tag.isEmpty()) {
			return messageRepo.findByTag(pageable, tag, user);
		} else {
			return messageRepo.findAll(pageable, user);
//			return messageDtoDao.findAll(pageable, user);
		}
	}

	public Page<MessageDto> messageListForUser(Pageable pageable, User author, User user) {
        return messageRepo.findByAuthor(pageable, author, user);
    }
	
	public void save(Message message) {
		messageRepo.save(message);
	}

	public void delete(Message message) {
		messageRepo.delete(message);
	}

}
