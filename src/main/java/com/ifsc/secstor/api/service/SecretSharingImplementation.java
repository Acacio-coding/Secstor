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
import com.ifsc.secstor.facade.ArchistarEngine;
import com.ifsc.secstor.facade.Engine;
import com.ifsc.secstor.facade.PVSSEngine;
import com.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;
import com.ufsc.das.gcseg.secretsharing.SecretShareEngine;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static com.ifsc.secstor.api.advice.messages.ErrorMessages.*;
import static com.ifsc.secstor.api.advice.paths.Paths.SECRET_SHARING_BASE_AND_RECONSTRUCT;
import static com.ifsc.secstor.api.advice.paths.Paths.SECRET_SHARING_BASE_AND_SPLIT;
import static com.ifsc.secstor.api.util.Constants.*;

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
                    NULL_DATA, SECRET_SHARING_BASE_AND_SPLIT);

        Object data;

        try {
            JSONObject baseObject = new JSONObject(splitDTO);
            data = baseObject.get(DATA);
        } catch (Exception exception) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    INVALID_DATA, SECRET_SHARING_BASE_AND_SPLIT);
        }

        if (splitDTO.getAlgorithm().equalsIgnoreCase(SHAMIR))
            return this.shamir.split(data.toString());

        if (splitDTO.getAlgorithm().equalsIgnoreCase(PSS))
            return this.pss.split(data.toString());

        if (splitDTO.getAlgorithm().equalsIgnoreCase(CSS))
            return this.css.split(data.toString());

        if (splitDTO.getAlgorithm().equalsIgnoreCase(KRAWCZYK))
            return this.krawczyk.split(data.toString());

        if (splitDTO.getAlgorithm().equalsIgnoreCase(PVSS))
            return this.pvss.split(data.toString());

        throw new ValidationException(HttpStatus.BAD_REQUEST,
                INVALID_ALGORITHM, SECRET_SHARING_BASE_AND_SPLIT);
    }

    @Override
    public String reconstruct(ReconstructDTO reconstructDTO) throws UnsupportedEncodingException,
            InvalidParametersException, InvalidVSSScheme, ReconstructionException {

        if (reconstructDTO.getSecret() == null || reconstructDTO.getSecret() == "")
            throw new ValidationException(HttpStatus.BAD_REQUEST, NULL_SECRET,
                    SECRET_SHARING_BASE_AND_RECONSTRUCT);

        JSONObject baseObject;
        JSONObject secret;

        try {
            baseObject = new JSONObject(reconstructDTO);
            secret = (JSONObject) baseObject.get(SECRET);
        } catch (Exception exception) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    INVALID_SECRET, SECRET_SHARING_BASE_AND_RECONSTRUCT);
        }

        if (secret.has(MACKEYS))
            return this.pss.reconstruct(secret);
        else if (secret.has(FINGERPRINTS))
            return this.css.reconstruct(secret);
        else if (secret.has(ENCKEYS))
            return this.krawczyk.reconstruct(secret);
        else if (secret.has(MODULUS))
            return this.pvss.reconstruct(secret);
        else if (secret.has(SHARES))
            return this.shamir.reconstruct(secret);

        throw new ValidationException(HttpStatus.BAD_REQUEST,
                NO_MATCH_SECRET, SECRET_SHARING_BASE_AND_RECONSTRUCT);
    }
}
