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
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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

        LocalDateTime time = LocalDateTime.now();
        log.info("file요청" + req.getRequestURI());
        log.info("----giveDataFileToCentral----");

        /*파일명으로 파일 찾기*/
        File f = new File(fileManager.getVehicleLocation().toString() + "/" + filehash + ".csv.gz");

        if (f.exists()) {

            /*
             프록시 환경 또는 클라우드 위에 있는 웹 응용 프로그램의 경우 HTTP 요청 헤더 X-Forwarded-For(XFF)를 통해 클라이언트 IP 주소를 가져와야 한다.
            WAS 는 보통 2차 방화벽 안에 있고 Web Server 를 통해 client 에서 호출되거나 cluster로 구성되어 load balancer 에서 호출된다.
            이럴 경우에서 getRemoteAddr() 을 호출하면 접근한 클라이언트의 외부아이피가 아닌 웹서버나 load balancer의 IP 가 나오면서 같은아이피가 계속 찍히게 된다.
            위와 같은 문제를 해결하기 위해 사용되는 HTTP Header인 X-Forwarded-For 값을 확인해서 있으면 해당 키값을 사용하고 없으면 getRemoteAddr() 를 사용한다.
            * */
            String clientIP = (null != req.getHeader("X-FORWARDED-FOR")) ? req.getHeader("X-FORWARDED-FOR") : req.getRemoteAddr();
            Resource resource = new FileSystemResource(f);
            long fileLength = f.length();

            /*
             * tracing history를 남기기 위해 edge로 소켓 통신
             * format
             * {[{sptoedge9812::DATA_ID::RECEIVED_TIME::CLIENT_IP::}]}
             */

            String reqString = "{[{sptoedge9812::" + filehash + "::" + time.toString() + "::" + clientIP + "::}]}";
            String hostIP = "127.0.0.1";
            int port = 17300;
            connectSocket(reqString, hostIP, port);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename() + "\"")
                    .contentLength(fileLength)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }

        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/data/{filehash}")
    @SneakyThrows
    public ResponseEntity<?> deleteAllDataInEdge(@PathVariable String filehash, HttpServletRequest req) {

        String reqString = "{[{sptoedge1108::" + filehash + "::}]}";
        String hostIP = "127.0.0.1";
        int port = 17300;
        connectSocket(reqString, hostIP, port);
        return new ResponseEntity<>(HttpStatus.OK);

    }


    private void connectSocket(String reqString, String hostIP, int port) {

        try (Socket socket = new Socket(hostIP, port);
             OutputStream output = socket.getOutputStream();) {
            byte[] data = reqString.getBytes(StandardCharsets.US_ASCII);
            output.write(data);
            output.flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
