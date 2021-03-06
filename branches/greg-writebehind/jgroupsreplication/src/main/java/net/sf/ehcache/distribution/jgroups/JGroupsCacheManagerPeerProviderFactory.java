/**
 *  Copyright 2003-2009 Terracotta, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package net.sf.ehcache.distribution.jgroups;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CacheManagerPeerProviderFactory;
import net.sf.ehcache.util.PropertyUtil;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pierre Monestie (pmonestie__REMOVE__THIS__@gmail.com)
 * @author <a href="mailto:gluck@gregluck.com">Greg Luck</a>
 * @version $Id$
 */

public class JGroupsCacheManagerPeerProviderFactory extends CacheManagerPeerProviderFactory {
    private static final Logger LOG = LoggerFactory.getLogger(JGroupsCacheManagerPeerProviderFactory.class.getName());
    private static final String CONNECT = "connect";

    /**
     * {@inheritDoc}
     */
    public CacheManagerPeerProvider createCachePeerProvider(CacheManager cacheManager, Properties properties) {
        LOG.debug("CREATING JGOUPS PEER PROVIDER");
        String connect = PropertyUtil.extractAndLogProperty(CONNECT, properties);
        if (connect == null) {
            connect = "";
        }

        connect = connect.replaceAll(" ", "");
        if (connect.trim().equals("")) {
            connect = null;
        }
        LOG.debug("Connect is: {}", connect);
        return new JGroupManager(cacheManager, connect);

    }

}
