package com.ifsc.secstor.api.service;

import com.at.archistar.crypto.CSSEngine;
import com.at.archistar.crypto.KrawczykEngine;
import com.at.archistar.crypto.PSSEngine;
import com.at.archistar.crypto.ShamirEngine;
import com.at.archistar.crypto.data.InvalidParametersException;
import com.at.archistar.crypto.secretsharing.ReconstructionException;
import com.at.archistar.crypto.secretsharing.WeakSecurityException;
import com.ifsc.secstor.api.advice.exception.ValidationException;
import com.ifsc.secstor.api.dto.ReconstructDTO;
import com.ifsc.secstor.api.dto.SplitDTO;
import com.ifsc.secstor.facade.*;
import com.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;
import com.ufsc.das.gcseg.secretsharing.SecretShareEngine;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class SecretSharingImplementation implements SecretSharingService {
    private final Engine shamir;
    private final Engine pss;
    private final Engine css;
    private final Engine krawczyk;
    private final Engine pvss;

    public SecretSharingImplementation(int n, int k) throws WeakSecurityException, NoSuchAlgorithmException {
        this.shamir = new ArchistarEngine(new ShamirEngine(n, k));
        this.pss = new ArchistarEngine(new PSSEngine(n, k));
        this.css = new ArchistarEngine(new CSSEngine(n, k));
        this.krawczyk = new ArchistarEngine(new KrawczykEngine(n, k));
        this.pvss = new PVSSEngine(new SecretShareEngine(n, k));
    }

    @Override
    public Object split(SplitDTO splitDTO) throws UnsupportedEncodingException, InvalidVSSScheme {
        if (splitDTO.getData() == null || splitDTO.getData() == "")
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Data must be provided", "/api/v1/secret-sharing/split");

        Object data;

        try {
            JSONObject baseObject = new JSONObject(splitDTO);
            data = baseObject.get("data");
        } catch (Exception exception) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Data provided is invalid, it must be an object", "/api/v1/secret-sharing/split");
        }

        if (splitDTO.getAlgorithm().equalsIgnoreCase("shamir"))
            return this.shamir.split(data.toString());

        if (splitDTO.getAlgorithm().equalsIgnoreCase("pss"))
            return this.pss.split(data.toString());

        if (splitDTO.getAlgorithm().equalsIgnoreCase("css"))
            return this.css.split(data.toString());

        if (splitDTO.getAlgorithm().equalsIgnoreCase("krawczyk"))
            return this.krawczyk.split(data.toString());

        if (splitDTO.getAlgorithm().equalsIgnoreCase("pvss"))
            return this.pvss.split(data.toString());

        throw new ValidationException(HttpStatus.BAD_REQUEST,
                "Algorithm provided is invalid, it must be either SHAMIR, PSS, CSS, KRAWCZYK or PVSS",
                "/api/v1/secret-sharing/split");
    }

    @Override
    public String reconstruct(ReconstructDTO reconstructDTO) throws UnsupportedEncodingException,
            InvalidParametersException, InvalidVSSScheme, ReconstructionException {

        if (reconstructDTO.getSecret() == null || reconstructDTO.getSecret() == "")
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Secret must be provided",
                    "/api/v1/secret-sharing/reconstruct");

        JSONObject baseObject;
        JSONObject secret;

        try {
            baseObject = new JSONObject(reconstructDTO);
            secret = (JSONObject) baseObject.get("secret");
        } catch (Exception exception) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Secret provided is invalid, it must be an object",
                    "/api/v1/secret-sharing/reconstruct");
        }

        if (secret.has("macKeys"))
            return this.pss.reconstruct(secret);
        else if (secret.has("fingerprints"))
            return this.css.reconstruct(secret);
        else if (secret.has("encKeys"))
            return this.krawczyk.reconstruct(secret);
        else if (secret.has("modulus"))
            return this.pvss.reconstruct(secret);
        else if (secret.has("shares"))
            return this.shamir.reconstruct(secret);

        throw new ValidationException(HttpStatus.BAD_REQUEST,
                "Secret provided doesn't match any of share algorithm types",
                "/api/v1/secret-sharing/reconstruct");
    }
}
