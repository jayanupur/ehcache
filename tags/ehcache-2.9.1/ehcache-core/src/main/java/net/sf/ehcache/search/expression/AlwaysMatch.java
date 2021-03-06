/**
 *  Copyright Terracotta, Inc.
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

package net.sf.ehcache.search.expression;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.attribute.AttributeExtractor;

/**
 * Criteria that always return true
 *
 * @author teck
 */
public class AlwaysMatch extends BaseCriteria {

    /**
     * {@inheritDoc}
     */
    public boolean execute(Element e, Map<String, AttributeExtractor> attributeExtractors) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Criteria and(Criteria other) {
        return other;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Criteria or(Criteria other) {
        return this;
    }
    
    @Override
    public Set<Attribute<?>> getAttributes() {
        return Collections.emptySet();
    }

}
