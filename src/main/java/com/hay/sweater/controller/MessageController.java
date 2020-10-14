package com.hay.sweater.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hay.sweater.domain.Message;
import com.hay.sweater.domain.User;
import com.hay.sweater.domain.dto.MessageDto;
import com.hay.sweater.repository.MessageRepo;
import com.hay.sweater.service.MessageService;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;

@Controller
public class MessageController {
	@Autowired
	private MessageRepo messageRepo;
	
	@Autowired
	private MessageService messageService;

	@Value("${upload.path}")
	private String uploadPath;

	@GetMapping("/")
	public String greeting(Map<String, Object> model) {
		return "greeting";
	}

	@GetMapping("/main")
	public String main(
			@RequestParam(required = false, defaultValue = "") String filter, 
			Model model,
			@PageableDefault(sort = { "id" }, direction = Direction.DESC) Pageable pageable,
			@AuthenticationPrincipal User currentUser
	) {
		Page<MessageDto> page = messageService.messageList(pageable, filter, currentUser);
		
		model.addAttribute("page", page);
		model.addAttribute("url", "/main");
		model.addAttribute("filter", filter);

		return "main";
	}

	@PostMapping("/main")
	public String add(
			@AuthenticationPrincipal User user, 
			@Valid Message message, 
			BindingResult bindingResult,
			Model model, 
			@RequestParam("file") MultipartFile file
	) throws IllegalStateException, IOException {
		message.setAuthor(user);

		if (bindingResult.hasErrors()) {
			Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
		} else {

			saveFile(message, file);
			
			model.addAttribute("message", null);
			
			messageRepo.save(message);
		}
		
		Iterable<Message> messages = messageRepo.findAll();

		model.addAttribute("messages", messages);

		return "main";
	}

	private void saveFile(Message message, MultipartFile file) throws IOException {
		if (file != null && !file.getOriginalFilename().isEmpty()) {
			File uploadDir = new File(uploadPath);

			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}

			String uuidFile = UUID.randomUUID().toString();
			String resultFilename = uuidFile + "." + file.getOriginalFilename();

			file.transferTo(new File(uploadPath + "/" + resultFilename));

			message.setFilename(resultFilename);
		}
	}
	
	@GetMapping("/user-messages/{author}")
	public String userMessages(
			@AuthenticationPrincipal User currentUser,
			@PathVariable User author,
			Model model,
			@RequestParam(required = false) Message message,
			@PageableDefault(sort = { "id" }, direction = Direction.DESC) Pageable pageable

	) {
		Page<MessageDto> page = messageService.messageListForUser(pageable, author, currentUser);
			
		model.addAttribute("userChannel", author);
		model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
		model.addAttribute("subscribersCount", author.getSubscribers().size());
		model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser) );
		model.addAttribute("page", page);
		model.addAttribute("message", message);
		model.addAttribute("isCurrentUser", currentUser.equals(author));
		model.addAttribute("url", "/user-messages/"+author.getId());
		
		return "userMessages";
	}

	@PostMapping("/user-messages/{user}")
	public String updateMessage(
			@AuthenticationPrincipal User currentUser,
			@PathVariable Long user,
			@RequestParam("id") Message message,
			@RequestParam("text") String text,
			@RequestParam("tag") String tag,
			@RequestParam("file") MultipartFile file
	) throws IOException {
		if (message==null) {
			message = new Message(text, tag, currentUser);
		}
		if ( message.getAuthor().equals(currentUser)) {
			if (!StringUtils.isEmpty(text)) {
				message.setText(text);
			}
			
			if (!StringUtils.isEmpty(tag)) {
				message.setTag(tag);
			}
			
			saveFile(message, file);
			messageRepo.save(message);
		}
		
		return "redirect:/user-messages/" + user;
	}

}
