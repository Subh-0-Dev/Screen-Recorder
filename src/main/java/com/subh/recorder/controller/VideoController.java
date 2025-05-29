package com.subh.recorder.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.subh.recorder.Services.SupabaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.subh.recorder.entities.UserEntity;
import com.subh.recorder.entities.Video;
import com.subh.recorder.repositories.UserRepository;
import com.subh.recorder.repositories.VideoRepository;



@Controller
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    private final Path uploadPath = Paths.get("uploads");
    
    @GetMapping("/video")
    public String video() {
        return "video";
    }
    
    @GetMapping("/user")
    public String userName(Principal principal) {
    	return principal.getName();
    }
    
    @GetMapping("/myvideos")
    public ResponseEntity<List<Video>> getMyVideos(Principal principal) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(principal.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserEntity user = userOpt.get();
        List<Video> videos = videoRepository.findByUser(user);
        return ResponseEntity.ok(videos);
    }

    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file, Principal principal) {
        try {
//            String uploadDir = System.getProperty("user.dir") + "/uploads/";
//            File uploadDirFile = new File(uploadDir);
//            if (!uploadDirFile.exists()) {
//                uploadDirFile.mkdirs();
//            }
//
//            String filePath = uploadDir + file.getOriginalFilename();
//            File dest = new File(filePath);
//            file.transferTo(dest);
            File tempFile = File.createTempFile("video", ".webm");
            file.transferTo(tempFile);

            String videoUrl = supabaseStorageService.uploadVideo(tempFile, file.getOriginalFilename());


            if (principal == null) {
                throw new RuntimeException("No logged-in user");
            }

            UserEntity user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

            Video video = new Video();
            video.setFileName(file.getOriginalFilename());
            video.setFilePath(videoUrl);
            video.setUploadedAt(LocalDateTime.now());
            video.setUser(user);

            videoRepository.save(video);

            return ResponseEntity.ok("Video uploaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Upload failed: " + e.getMessage());
        }
    }


}

