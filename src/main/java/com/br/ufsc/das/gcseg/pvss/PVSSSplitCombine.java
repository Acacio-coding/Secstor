package com.br.ufsc.das.gcseg.pvss;

import com.br.ufsc.das.gcseg.pvss.engine.PVSSEngine;
import com.br.ufsc.das.gcseg.pvss.engine.PublicInfoPVSS;
import com.br.ufsc.das.gcseg.pvss.engine.PublishedShares;
import com.br.ufsc.das.gcseg.pvss.engine.Share;
import com.br.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;
import com.br.ufsc.das.gcseg.secretsharing.SharestoCombine;
import com.br.ufsc.das.gcseg.secretsharing.SplitedShares;
import com.google.common.io.BaseEncoding;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;

public class PVSSSplitCombine {
	
	int n,t;
	
	public PVSSSplitCombine(int n, int t){
		this.n = n;
		this.t = t;
	}
	
	public SplitedShares pVSSSplit(String secret) throws InvalidVSSScheme {

		int num_bits = 512;// Key Size

		SecureRandom random = new SecureRandom();

		 BigInteger groupPrimeOrder = BigInteger.probablePrime(num_bits, random);
		// Using a static groupPrimeOrder is a insecure but easy solution
//		BigInteger groupPrimeOrder = new BigInteger("5750150673812729126491235878127157217819341108488091306437");

		BigInteger g1 = BigInteger.probablePrime(num_bits - 1, random);
		BigInteger g2 = BigInteger.probablePrime(num_bits - 2, random);

		// Generate public info
		PublicInfoPVSS pi = new PublicInfoPVSS(n, t, groupPrimeOrder, g1, g2);

		// Create an instance of the Engine
		PVSSEngine engine = new PVSSEngine(pi);

		// Generate the Secret keys
		BigInteger[] secretKeys = engine.generateSecretKeys();

		// Generate Public Keys
		// TODO Could be the the same way as the Secret Keys
		BigInteger[] publicKeys = new BigInteger[n];
		for (int i = 0; i < n; i++) {
			publicKeys[i] = engine.generatePublicKey(secretKeys[i]);
		}

		// Generate the shares, proves, etc
		PublishedShares publishedShares = engine.generalPublishShares(secret.getBytes(), publicKeys);

		SplitedShares ss = new SplitedShares();
		ss.setModulus(groupPrimeOrder);

		// Store parts of the share
		for (int i = 0; i < n; i++) {
			// shares[i] = publishedShares.getShare(i, secretKeys[i], pi,
			// publicKeys);
			ss.addShare(publishedShares.getShare(i, secretKeys[i], pi, publicKeys).getShare().toString());
		}

		// Convert the general key to String
		String key = BaseEncoding.base64().encode(publishedShares.getShare(0, secretKeys[0], pi, publicKeys).getU());

		ss.setKey(key);

		return ss;

	}

	public String pVSScombine(SharestoCombine genShares)
			throws InvalidVSSScheme, UnsupportedEncodingException {

		// Create an instance of the Engine
		PublicInfoPVSS pi = new PublicInfoPVSS(n, t, genShares.getModulus(), null, null);

		PVSSEngine engine = new PVSSEngine(pi);

		Share[] shares = new Share[genShares.getShareString().size()];
		
//		System.out.println(genShares.getKey());

		byte[] key = BaseEncoding.base64().decode (genShares.getKey());

		for (int i = 0; i < genShares.getShareString().size(); i++) {
			String parti = genShares.getShareString().get(i);
			shares[i] = new Share(i, null, new BigInteger(parti), null, null, key);
		}

		byte[] result = engine.generalCombineShares(shares);

		//System.out.println("Combined share: " + new String(result));

		return new String(result);
	}
	
	public boolean pVSSValidate(SharestoCombine genShares) throws InvalidVSSScheme{
		
		// Create an instance of the Engine
		PublicInfoPVSS pi = new PublicInfoPVSS(n, t, genShares.getModulus(), null, null);

		PVSSEngine engine = new PVSSEngine(pi);

		Share[] shares = new Share[genShares.getShareString().size()];
		
//		System.out.println(genShares.getKey());

		byte[] key = BaseEncoding.base64().decode (genShares.getKey());

		for (int i = 0; i < genShares.getShareString().size(); i++) {
			String parti = genShares.getShareString().get(i);
			shares[i] = new Share(i, null, new BigInteger(parti), null, null, key);
		}

		byte[] result = engine.generalCombineShares(shares);

		//System.out.println("Combined share: " + new String(result));
		
		return false;
		
	}

}
