package com.ufsc.das.gcseg.secretsharing;

import com.ufsc.das.gcseg.pvss.PVSSSplitCombine;
import com.ufsc.das.gcseg.pvss.exception.InvalidVSSScheme;

import java.io.UnsupportedEncodingException;

public class SecretShareEngine {

	int n, k;

	/**
	* Create an object for the SecretShareEngine 
	*
	* @param  n  number of part to be divided
	* @param  k mininum parts to restore the secret
	* 	*/
	public SecretShareEngine(int n, int k) {
		this.n = n;
		this.k = k;
	}

	/**
	* Split the secret into n parts
	*
	* @param  secret secret string
	* @return Shares on a SplitedShares object
	* 	*/	
	public SplitedShares split(String secret) throws UnsupportedEncodingException, InvalidVSSScheme {
		PVSSSplitCombine psc = new PVSSSplitCombine(n, k);
		return psc.pVSSSplit(secret);
	}

	/**
	* Combine shares and return the secret
	*
	* @param  genShares shares to be combined. You need to create a list of shares
	* @return  secret string
	* 	*/	
	public String combine(SharestoCombine genShares) throws InvalidVSSScheme{
		PVSSSplitCombine psc = new PVSSSplitCombine(n, k);
		return psc.pVSScombine(genShares);
	}

	@Override
	public String toString() {
		return "PVSS";
	}
}
