package com.ubaidsample.SBJPASSThymeleafImageUpload.controller;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ubaidsample.SBJPASSThymeleafImageUpload.model.Image;
import com.ubaidsample.SBJPASSThymeleafImageUpload.model.User;
import com.ubaidsample.SBJPASSThymeleafImageUpload.repository.ImageRepository;
import com.ubaidsample.SBJPASSThymeleafImageUpload.repository.UserRepository;

@Controller
public class ImageController {

	private UserRepository userRepository;
	private ImageRepository imageRepository;
	private Path rootLocation;

	public ImageController(UserRepository userRepository, Path rootLocation, ImageRepository imageRepository) {
		this.userRepository = userRepository;
		this.rootLocation = rootLocation;
		this.imageRepository = imageRepository;
	}

	@GetMapping("/")
	public String listUploadedImages(Model model, Principal principal) throws Exception {

		if (principal == null) {
			return "redirect:/find";
		}

		User user = userRepository.findByName(principal.getName()).orElseThrow(() -> new Exception());

		List<String> stringss = user.getImageList().stream()
				.map(image -> this.rootLocation.resolve(image.getName()))
				.map(path -> MvcUriComponentsBuilder
						.fromMethodName(ImageController.class, "serveFile", path.getFileName().toString()).build()
						.toString())				
				.collect(Collectors.toList());


		model.addAttribute("files", stringss);

		return "uploadImage";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {

		Path file = this.rootLocation.resolve(filename);
		Resource resource = new UrlResource(file.toUri());

		return ResponseEntity
				.ok()
				.body(resource);	
	}

	@PostMapping("/")
	public String handleUploadImages(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
			Principal principal) throws Exception {

		if (file.getSize() == 0) {
			return "redirect:/";
		}

		String uuid = UUID.randomUUID().toString();

		String imagePath = this.rootLocation.resolve(uuid + ".jpg").toString();

		User user = userRepository.findByName(principal.getName()).orElseThrow(() -> new Exception());

		Set<Image> stringList = user.getImageList();
		stringList.add(new Image(imagePath));
		Files.copy(file.getInputStream(), this.rootLocation.resolve(imagePath));
		
		userRepository.save(user);

		return "redirect:/";
	}

	@GetMapping("/find")
	public String findImages(Model model) {
		return "findImage";
	}
	
	@GetMapping("/search")
	public String findImages(@RequestParam("name") String name, Model model)  {

		User user;
		try {
			user = userRepository.findByName(name).orElseThrow(Exception::new);
		} catch (Exception e) {
			return "redirect:/find";
		}
		
		List<String> userImages = user.getImageList().stream()
				.map(image -> this.rootLocation.resolve(image.getName()))
				.map(path -> MvcUriComponentsBuilder
						.fromMethodName(ImageController.class, "serveFile", path.getFileName().toString()).build()
						.toString())				
				.collect(Collectors.toList());
		
		model.addAttribute("files", userImages);
		model.addAttribute("user", user.getName());

		return "findImage";

	}

	@RequestMapping("/delete")
	public String findImages(Principal principal, @RequestParam("text") String text, String string) throws Exception {

		User user = userRepository.findByName(principal.getName()).orElseThrow(() -> new Exception());

		text = text.substring(text.lastIndexOf("/"));
		text = this.rootLocation + text;

		Image image = imageRepository.findByName(text);

		user.getImageList().remove(image);

		userRepository.save(user);

		return "redirect:/";

	}
}
