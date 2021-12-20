package com.penta.edgesupporter.controller;

import com.penta.edgesupporter.configuration.FileUploadProperties;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
@Getter
@Slf4j
public class FileManager {

    private final Path vehicleLocation; // vehicle 송신 파일 저장 경로
    private final Path certLocation; // 수신한 인증서 파일 저장 경로

    public FileManager(FileUploadProperties properties) {
        this.vehicleLocation = Paths.get(properties.getVehicle()).toAbsolutePath().normalize();
        this.certLocation = Paths.get(properties.getCert()).toAbsolutePath().normalize();
    }

    @PostConstruct
    @SneakyThrows
    public void init() {
        Files.createDirectories(vehicleLocation);
    }

    @SneakyThrows({NoSuchAlgorithmException.class, IOException.class})
    private String getHash(InputStream inputStream) {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = inputStream.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        ;

        //close the stream; We don't need it now.
        inputStream.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();

        /*
        // MD5
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        byte[] dataBytes = new byte[1024];
        Integer nRead = 0;
        while ((nRead = inputStream.read(dataBytes)) != -1) {
            messageDigest.update(dataBytes, 0, nRead);
        }
        byte[] mdBytes = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (Integer i = 0; i < mdBytes.length; i++) {
            stringBuffer.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16)).substring(1);
        }
        return stringBuffer.toString();
         */

    }

    @SneakyThrows
    public String getHash(String target) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(target.getBytes("UTF-8"));
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    private String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public String getHex(byte[] bytes) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            hexStringBuffer.append(byteToHex(bytes[i]));
        }
        return hexStringBuffer.toString();
    }


    @SneakyThrows(FileNotFoundException.class)
    public String getHash(File file) {
        return getHash(new FileInputStream(file));
    }

    @SneakyThrows({IOException.class})
    public String getHash(MultipartFile file) {
        return getHash(file.getInputStream());
    }
}
