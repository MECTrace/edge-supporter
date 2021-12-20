package com.penta.edgesupporter.controller;

import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

@RequestMapping("/api/edge")
@RestController
public class SignatureController {

    // VehicleCert.builder().certAlias("car-1").certPath(certPath+"car-1.p12").certPassword(certPassword).trustStorePath(trustStorePath).trustStorePassword(trustStorePassword).build());

    private final FileManager fileManager;
    private final String certAlias = "car-1";
    private final String certPath = "/home/mecTrace/penta/certificate/car-1.p12";
    private final String certPassword = "pentaketi0415";

    public SignatureController(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @PostMapping(value = "/signature")
    @SneakyThrows
    public ResponseEntity<?> makeFileSignatrue(@RequestParam("file") MultipartFile[] files) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        for(MultipartFile file : files) {
            byte[] signatureBytes = getSignatureResource(file.getBytes()).getByteArray();
            String signatureValue = fileManager.getHex(signatureBytes);
            body.add("fileName", file.getName());
            body.add("signature", signatureValue);
        }

        return  new ResponseEntity<Object>(body, HttpStatus.OK);

    }


    @SneakyThrows
    private PrivateKey getPrivateKey() {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(certPath), certPassword.toCharArray());
        return (PrivateKey) keyStore.getKey(certAlias, certPassword.toCharArray());
    }


    @SneakyThrows
    private ByteArrayResource getSignatureResource(byte[] fileBytes) {
        // 서명
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(getPrivateKey());

        signature.update(fileBytes);

        byte[] digitalSignature = signature.sign();

        // * Getter 필요
        ByteArrayResource resource = new ByteArrayResource(digitalSignature) {
            @Override
            public String getFilename() {
                return "signature";
            }
        };

        return resource;
    }
}
