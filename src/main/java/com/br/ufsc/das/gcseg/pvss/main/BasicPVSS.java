package com.br.ufsc.das.gcseg.pvss.main;

import com.br.ufsc.das.gcseg.pvss.engine.PVSSEngine;
import com.br.ufsc.das.gcseg.pvss.engine.PublicInfoPVSS;
import com.br.ufsc.das.gcseg.pvss.engine.PublishedShares;
import com.br.ufsc.das.gcseg.pvss.engine.Share;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 *
 * @author alysson
 */
public class BasicPVSS {

	static final int N = 3;
	static final int T = 3;
	static final int NUM_BITS = 192;

	static final SecureRandom random = new SecureRandom();

	public static void main(String[] args) throws Exception {

		// inicializacao
		PVSSEngine engine = PVSSEngine.getInstance(N, T, NUM_BITS);

		PublicInfoPVSS info = engine.getPublicInfo();

		BigInteger[] secretKeys = engine.generateSecretKeys();

		BigInteger[] publicKeys = new BigInteger[N];

		for (int i = 0; i < N; i++) {
			publicKeys[i] = engine.generatePublicKey(secretKeys[i]);
		}

		System.out.println("secret keys: " + Arrays.toString(secretKeys));
		System.out.println("public keys: " + Arrays.toString(publicKeys));

		// distribuicao
		BigInteger secret = engine.generateSecret();

		System.out.println("secret: " + secret);

		BigInteger expectedResult = engine.getPublicInfo().getGeneratorG().modPow(secret,
				engine.getPublicInfo().getGroupPrimeOrder());

		System.out.println("encriptedSecret: " + expectedResult);

		long t0 = System.currentTimeMillis();
		PublishedShares publishedShares = engine.publishShares(secret, null, publicKeys);
		long t1 = System.currentTimeMillis();

		System.out.println("passou: " + (t1 - t0) + "ms");

		System.out.println("published shares: " + publishedShares);

		t0 = System.currentTimeMillis();
		System.out.println(publishedShares.verify(info, publicKeys) ? "valid shares" : "invalid shares");
		t1 = System.currentTimeMillis();

		System.out.println("passou: " + (t1 - t0) + "ms");

		Share[] shares = new Share[N];

		for (int i = 0; i < N; i++) {
			shares[i] = publishedShares.getShare(i, secretKeys[i], info, publicKeys);
			System.out.println(shares[i] + (shares[i].verify(info, publicKeys[i]) ? " valid" : "invalid"));
		}

		int[] x = new int[T];

		for (int i = 0; i < T; i++) {
			x[i] = i;
		}

		t0 = System.currentTimeMillis();
		BigInteger result = engine.combineShares(x, shares);
		t1 = System.currentTimeMillis();
		System.out.println("passou: " + (t1 - t0) + "ms");

		System.out.println("result encripted secret: " + result);
	}
}
