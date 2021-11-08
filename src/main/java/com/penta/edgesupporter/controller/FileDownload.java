package com.penta.edgesupporter.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/edge")
@Slf4j
@CrossOrigin("*")
public class FileDownload {

    private final FileManager fileManager;

    @GetMapping(value = "/download/datafile/{filehash}")
    @SneakyThrows
    public ResponseEntity<?> giveDataFileToCentral(@PathVariable String filehash, HttpServletRequest req) {

        log.info("file요청" + req.getRequestURI());
        log.info("----giveDataFileToCentral----");

        File dir = new File(fileManager.getVehicleLocation().toString());
        File[] fileList = dir.listFiles();

        long fileLength = 0L;

        for(File file : fileList) {
            if(filehash.equals(fileManager.getHash(file))) {
                log.info("일치하는 파일 :: {}", file.getName());
                Resource resource = new FileSystemResource(file);
                fileLength = file.length();

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename() + "\"")
                        .contentLength(fileLength)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            }
        }

        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }


}
