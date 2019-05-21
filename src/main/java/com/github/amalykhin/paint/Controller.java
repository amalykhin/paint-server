package com.github.amalykhin.paint;

import com.github.amalykhin.paint.service.PictureService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    PictureService pictureService;

    @GetMapping(value = "/pictures", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<List<String>> getPicture() throws IOException {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(pictureService.getPicturesList());
    }

    @GetMapping(value = "/pictures/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getPicture(@PathVariable String name) throws IOException {
        /*
        InputStream is = getClass().getResourceAsStream("/pictures/" + id + ".png");
        return IOUtils.toByteArray(is);
        */
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(pictureService.getPictureResource(name));
    }

    @PostMapping(value = "/pictures")
    public ResponseEntity storePicture(@RequestParam("file") MultipartFile file) {
        System.out.println("Storing the picture");
        try {
            Resource pictureResource = pictureService.storePicture(file);
            ResponseEntity re = ResponseEntity.created(pictureResource.getURI())
                .body("");
            System.out.println(re);
            return re;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("");
    }
}
