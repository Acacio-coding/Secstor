package com.ifsc.secstor.api.controller;

import com.at.archistar.crypto.data.InvalidParametersException;
import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.at.archistar.crypto.secretsharing.WeakSecurityException;
import com.ifsc.secstor.api.dto.ReconstructDTO;
import com.ifsc.secstor.api.dto.SplitDTO;
import com.ifsc.secstor.api.service.SecretSharingImplementation;
import com.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/secret-sharing")
public class SecretSharingController {

    private final SecretSharingImplementation secretSharingService;

    public SecretSharingController() throws WeakSecurityException, NoSuchAlgorithmException {
        this.secretSharingService = new SecretSharingImplementation(10, 5);
    }

    @PostMapping("/split")
    public ResponseEntity<Object> split(@RequestBody @Validated SplitDTO splitDTO) throws UnsupportedEncodingException, InvalidVSSScheme {
        return ResponseEntity.status(HttpStatus.OK).body(this.secretSharingService.split(splitDTO));
    }

    @PostMapping("/reconstruct")
    public ResponseEntity<Object> reconstruct(@RequestBody ReconstructDTO reconstructDTO) throws UnsupportedEncodingException,
            InvalidParametersException, InvalidVSSScheme, ReconstructionException {

        return ResponseEntity.status(HttpStatus.OK).body(this.secretSharingService.reconstruct(reconstructDTO));
    }
}