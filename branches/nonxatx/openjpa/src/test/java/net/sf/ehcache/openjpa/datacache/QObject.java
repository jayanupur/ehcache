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

package net.sf.ehcache.openjpa.datacache;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.openjpa.persistence.DataCache;

/**
 * @author Craig Andrews
 * @author Greg Luck
 */
@Entity
@DataCache
public class QObject {
    private static long idCounter = System.currentTimeMillis();

    @Id
    private long id;

    private String value;

    public QObject() {
        this("");
    }

    public QObject(String value) {
        id = idCounter++;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }


}
