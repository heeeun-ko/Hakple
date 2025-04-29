package com.golden_dobakhe.HakPle.domain.resource.image.service;

import com.golden_dobakhe.HakPle.domain.post.post.entity.Board;
import com.golden_dobakhe.HakPle.domain.post.post.repository.BoardRepository;
import com.golden_dobakhe.HakPle.domain.resource.image.entity.Image;
import com.golden_dobakhe.HakPle.domain.resource.image.repository.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileService {

    private final ImageRepository imageRepository;
    private final BoardRepository boardRepository;
    private final AmazonS3 amazonS3;

    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    
    public FileService(ImageRepository imageRepository, BoardRepository boardRepository, AmazonS3 amazonS3) {
        this.imageRepository = imageRepository;
        this.boardRepository = boardRepository;
        this.amazonS3 = amazonS3;
    }

  
    @Transactional
    public Map<String, Object> saveFileWithTempId(MultipartFile file, boolean saveEntity, Long boardId, String tempId) {
        try {
            String fileName = generateFileName(file);
            String s3Key = "temp/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucketName, s3Key, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            String fileUrl = amazonS3.getUrl(bucketName, s3Key).toString();

            Long imageId = null; 
            if (saveEntity) {
                Image image = Image.builder()
                    .filePath(fileUrl) 
                    .tempId(tempId) 
                    .isTemp(true) 
                    .isDeleted(false) 
                    .originalName(file.getOriginalFilename()) 
                    .storedName(s3Key) 
                    .path(s3Key) 
                    .size(file.getSize()) 
                    .contentType(file.getContentType()) 
                    .isTemporary(true) 
                    .expiresAt(LocalDateTime.now().plusHours(24)) 
                    .s3Key(s3Key) 
                    .build();

                if (boardId != null) {
                    Optional<Board> boardOpt = boardRepository.findById(boardId);
                    if (boardOpt.isPresent()) {
                        image.setBoard(boardOpt.get());
                    } else {
                    }
                }

                Image savedImage = imageRepository.save(image);
                imageId = savedImage.getId();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("filePath", fileUrl); 
            result.put("url", fileUrl);      
            result.put("tempId", tempId);    
            if (imageId != null) {
                result.put("imageId", imageId); 
            }

            return result;
        } catch (IOException e) { 
            throw new RuntimeException("파일 저장 실패 (I/O): " + e.getMessage(), e);
        } catch (Exception e) { 
            throw new RuntimeException("서버 처리 중 오류가 발생했습니다: " + e.getMessage(), e); 
        }
    }
    
    @Transactional
    public int linkImagesToBoard(List<String> tempIds, Long boardId) {
        if (tempIds == null || tempIds.isEmpty() || boardId == null) {
            return 0;
        }
        
        if (!boardRepository.existsById(boardId)) {
            throw new RuntimeException("게시글이 존재하지 않습니다: " + boardId);
        }
        
        List<Image> images = imageRepository.findByTempIdIn(tempIds);
        Board board = boardRepository.findById(boardId).orElseThrow();
        
        for (Image image : images) {
            try {
                String tempS3Url = image.getFilePath();
                String tempS3Key = extractS3KeyFromUrl(tempS3Url); 
                String fileName = tempS3Key.substring(tempS3Key.lastIndexOf('/') + 1);
                String permanentS3Key = "board/" + boardId + "/" + fileName; 

                CopyObjectRequest copyReq = new CopyObjectRequest(bucketName, tempS3Key, bucketName, permanentS3Key)
                        .withCannedAccessControlList(CannedAccessControlList.PublicRead); 
                amazonS3.copyObject(copyReq);

                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, tempS3Key));

                String permanentS3Url = amazonS3.getUrl(bucketName, permanentS3Key).toString();
                image.setFilePath(permanentS3Url);
                image.setBoard(board);
                image.setIsTemp(false);

            } catch (Exception e) {
            }
        }
        
        imageRepository.saveAll(images); 
        
        return images.size(); 
    }
    
    @Transactional
    public void deleteTempImages(List<String> tempIds) {
        if (tempIds == null || tempIds.isEmpty()) return;
        List<Image> images = imageRepository.findByTempIdIn(tempIds);
        for (Image img : images) {
            String s3Key = extractS3KeyFromUrl(img.getFilePath());
            if (s3Key != null && s3Key.startsWith("temp/")) { 
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, s3Key));
            } else {
            }
            imageRepository.delete(img);
        }
    }

    @Transactional
    public void cleanUpUnused(Long boardId, List<String> usedImageUrls) {
        if (boardId == null || usedImageUrls == null) return;

        Set<String> usedUrlsSet = new HashSet<>();
        for (String url : usedImageUrls) {
            try {
                usedUrlsSet.add(URLDecoder.decode(url, StandardCharsets.UTF_8));
            } catch (IllegalArgumentException e) {
                usedUrlsSet.add(url); 
            }
        }

        List<Image> images = imageRepository.findByBoardId(boardId);

        for (Image img : images) {
            String decodedDbUrl = null;
            try {
                decodedDbUrl = URLDecoder.decode(img.getFilePath(), StandardCharsets.UTF_8);
            } catch (IllegalArgumentException e) {
            }

            if (!usedUrlsSet.contains(decodedDbUrl)) {
                String s3Key = extractS3KeyFromUrl(img.getFilePath());
                if (s3Key != null) {
                    try {
                        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, s3Key));
                    } catch (Exception e) {
                        continue; 
                    }
                } else {
                }
                try {
                    imageRepository.delete(img);
                } catch (Exception e) {
                    throw new RuntimeException("Error deleting image from repository: " + e.getMessage(), e);
                }
            }
        }
    }

    
    @Transactional
    public void cleanupExpiredTemporaryImages() {
        LocalDateTime now = LocalDateTime.now();

        List<Image> expiredImages = imageRepository.findByIsTemporaryTrueAndExpiresAtBefore(now);

        for (Image image : expiredImages) {

            String s3Key = image.getS3Key(); 
            if (s3Key != null && s3Key.startsWith("temp/")) { 
                try {
                    amazonS3.deleteObject(new DeleteObjectRequest(bucketName, s3Key));
                } catch (Exception e) {
                    continue;
                }
            } else {
            }

            try {
                imageRepository.delete(image);
            } catch (Exception e) {
                throw new RuntimeException("Error deleting image from repository: " + e.getMessage(), e);
            }
        }
    }

    @Transactional
    public void cleanTempImagesNotIn(List<String> keepTempIds, String content) {
        Set<String> usedUrls = new HashSet<>();
        if (content != null) {
            Pattern pattern = Pattern.compile("<img[^>]+src=[\\\"']([^\\\"']+)[\\\"']");
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                try {
                    usedUrls.add(URLDecoder.decode(matcher.group(1), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    usedUrls.add(matcher.group(1)); 
                }
            }
        }

        List<Image> temps = imageRepository.findByBoardIsNull();

        for (Image img : temps) {
            if (Boolean.TRUE.equals(img.getIsTemp())) { 
                boolean keepById = keepTempIds != null && keepTempIds.contains(img.getTempId());
                
                String decodedDbUrl = null;
                try {
                    decodedDbUrl = URLDecoder.decode(img.getFilePath(), StandardCharsets.UTF_8);
                } catch (Exception e) {
                }
                boolean keepByUrl = usedUrls.contains(decodedDbUrl);
                
                if (!keepById && !keepByUrl) { 
                    try {
                        String s3Key = extractS3KeyFromUrl(img.getFilePath());
                        if (s3Key != null && s3Key.startsWith("temp/")) { 
                            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, s3Key));
                        } else {
                        }
                    } catch (Exception e) {
                    } 
                    try {
                        imageRepository.delete(img); 
                    } catch (Exception e) {
                        throw new RuntimeException("Error deleting image from repository: " + e.getMessage(), e);
                    }
                }
            }
        }
    }

 
    private String extractS3KeyFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }
        try {
            String prefix1 = "https://" + bucketName + ".s3." + amazonS3.getRegionName() + ".amazonaws.com/";
            String prefix2 = "https://" + bucketName + ".s3.amazonaws.com/";

            String key = null;
            if (fileUrl.startsWith(prefix1)) {
                key = fileUrl.substring(prefix1.length());
            } else if (fileUrl.startsWith(prefix2)) {
                key = fileUrl.substring(prefix2.length());
            }

            if (key != null) {
                return URLDecoder.decode(key, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
        }
        return null; 
    }

    private String generateFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        return UUID.randomUUID().toString() + extension;
    }
}
