package net.sf.ehcache.management.resource.services;

import net.sf.ehcache.management.EmbeddedEhcacheServiceLocator;
import net.sf.ehcache.management.resource.CacheManagerConfigEntity;
import net.sf.ehcache.management.resource.ConfigContainerEntity;
import net.sf.ehcache.management.service.EntityResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.management.resource.AgentEntity;
import org.terracotta.management.resource.services.validator.RequestValidator;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author brandony
 */
@Path("/agents/cacheManagers/configs")
public final class CacheManagerConfigsResourceServiceImpl implements CacheManagerConfigsResourceService {
  private static final Logger LOG = LoggerFactory.getLogger(CacheManagerConfigsResourceService.class);

  private final EntityResourceFactory entityResourceFactory;

  private final RequestValidator validator;

  public CacheManagerConfigsResourceServiceImpl() {
    EntityResourceFactory.Locator entityRsrcFactoryLocator = EmbeddedEhcacheServiceLocator.locator();
    this.entityResourceFactory = entityRsrcFactoryLocator.locateEntityResourceFactory();
    RequestValidator.Locator reqValidatorLocator = EmbeddedEhcacheServiceLocator.locator();
    this.validator = reqValidatorLocator.locateRequestValidator();
  }

  @Override
  public Response getXMLCacheManagerConfigs(UriInfo info) {
    LOG.info(String
        .format("Invoking CacheManagerConfigsResourceServiceImpl.getXMLCacheManagerConfigs: %s", info.getRequestUri()));

    validator.validateSafe(info);

    String names = info.getPathSegments().get(1).getMatrixParameters().getFirst("names");
    Set<String> cmNames = names == null ? null : new HashSet<String>(Arrays.asList(names.split(",")));

    Collection<CacheManagerConfigEntity> configs = entityResourceFactory.createCacheManagerConfigEntities(cmNames);

    ConfigContainerEntity<CacheManagerConfigEntity> cc = new ConfigContainerEntity<CacheManagerConfigEntity>();
    cc.setConfiguration(configs);
    cc.setAgentId(AgentEntity.EMBEDDED_AGENT_ID);

    return Response.ok(cc).build();
  }
}
