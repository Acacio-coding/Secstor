package com.at.archistar.crypto.informationchecking;

import com.at.archistar.crypto.data.InformationCheckingShare;
import com.at.archistar.crypto.data.InvalidParametersException;

import java.util.List;
import java.util.Map;

/**
 * <p>Secret-sharing splits up the original secret data into n shares, k of
 * which are needed to reconstruct the original secret. Basic algorithms expect
 * shares to either be available and thus not corrupted or unavailable.
 * Information checking allows detection of altered shares. This can be
 * utilized by implementations to select k uncorrupted shares for reconstruction</p>
 *
 * <p>An information checking algorithm takes a collection of shares and adds
 * share validation information. It should set the share's informationChecking,
 * macKeys and macs member variables. In addition it is allowed to add additional
 * data to the share's metadata collection.</p>
 */
public interface InformationChecking {

    /**
     * @param shares the shares with IC information to be checked
     * @return the shares that passed the IC check
     */
    Map<Boolean, List<InformationCheckingShare>> checkShares(InformationCheckingShare[] shares);

    /**
     * @param shares the shares that need IC information to be added
     * @return shares with information checking data
     */
    InformationCheckingShare[] createTags(InformationCheckingShare[] shares) throws InvalidParametersException;
}
