package com.br.ufsc.das.gcseg.pvss.main;

import com.br.ufsc.das.gcseg.pvss.engine.PVSSEngine;
import com.br.ufsc.das.gcseg.pvss.engine.PublicInfoPVSS;
import com.br.ufsc.das.gcseg.pvss.engine.PublishedShares;
import com.br.ufsc.das.gcseg.pvss.engine.Share;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author alysson
 */
public class GeneralPVSS {

	static final int N = 4;
	static final int T = 2;
	static final int NUM_BITS = 192;

	static final SecureRandom random = new SecureRandom();

	public static void main(String[] args) throws Exception {
		
		PVSSEngine engine = PVSSEngine.getInstance(N, T, NUM_BITS);

		PublicInfoPVSS info = engine.getPublicInfo();

		BigInteger[] secretKeys = engine.generateSecretKeys();

		BigInteger[] publicKeys = new BigInteger[N];
		for (int i = 0; i < N; i++) {
			publicKeys[i] = engine.generatePublicKey(secretKeys[i]);
		}

		// System.out.println("secret keys: "+Arrays.toString(secretKeys));
		// System.out.println("public keys: "+Arrays.toString(publicKeys));

		for (int z = 0; z < 100; z++) {

			long t0 = System.nanoTime();
			PublishedShares publishedShares = engine.generalPublishShares("Segredos".getBytes(), publicKeys);
			long t1 = System.nanoTime();

			System.out.println("time to generate the shares: " + (t1 - t0) / 1000 + "mis");

			// System.out.println(publishedShares);

			t0 = System.nanoTime();
			System.out.println(publishedShares.verify(info, publicKeys) ? "valid shares" : "invalid shares");
			t1 = System.nanoTime();

			System.out.println("time to verify received share: " + (t1 - t0) / 1000 + "mis");

			Share[] shares = new Share[N];

			for (int i = 0; i < N; i++) {
				t0 = System.nanoTime();
				shares[i] = publishedShares.getShare(i, secretKeys[i], info, publicKeys);
				t1 = System.nanoTime();
				System.out.println("time to extract collected share " + i + ": " + (t1 - t0) / 1000 + "mis");

				t0 = System.nanoTime();
				String validity = shares[i].verify(info, publicKeys[i]) ? " valid" : " invalid";
				t1 = System.nanoTime();
				System.out.println("time to verify collected share " + i + ": " + (t1 - t0) / 1000 + "mis");

				System.out.println(shares[i] + validity);
			}

			t0 = System.nanoTime();

			// shares[0] = null;
			shares[2] = null;

			byte[] result = engine.generalCombineShares(shares);
			t1 = System.nanoTime();

			System.out.println("time to combine shares: " + (t1 - t0) / 1000 + "mis");

			System.out.println("Resultado final: " + new String(result));
		}
	}
}
