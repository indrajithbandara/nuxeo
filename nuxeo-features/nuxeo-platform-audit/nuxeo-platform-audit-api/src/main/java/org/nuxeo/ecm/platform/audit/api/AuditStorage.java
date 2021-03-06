/*
 * (C) Copyright 2017 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Funsho David
 *
 */

package org.nuxeo.ecm.platform.audit.api;

import org.nuxeo.ecm.core.api.ScrollResult;

import java.util.List;
import java.util.function.Consumer;

/**
 * Audit storage interface to append {@link LogEntry} represented as JSON, and scroll saved entries.
 * 
 * @since 9.3
 */
public interface AuditStorage {

    void append(List<String> jsonEntries);

    ScrollResult scroll(AuditQueryBuilder queryBuilder, int batchSize, int keepAlive);

    ScrollResult scroll(String scrollId);

}
