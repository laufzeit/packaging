/**
 * Copyright (c) 2002-2011 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.server.rest.discovery;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;

import javax.ws.rs.core.Response;

import org.apache.commons.configuration.Configuration;
import org.junit.Test;
import org.neo4j.server.configuration.Configurator;
import org.neo4j.server.rest.discovery.DiscoveryService;
import org.neo4j.server.rest.repr.formats.JsonFormat;
import org.neo4j.server.rest.web.EntityOutputFormat;


public class DiscoveryServiceTest {
    
    @Test
    public void shouldReturnValidJSONWithDataAndManagementUris() throws Exception {
        Configuration mockConfig = mock(Configuration.class);
        String managementUri = "http://localhost:9999/management";
        when(mockConfig.getString(Configurator.WEB_ADMIN_PATH_PROPERTY_KEY)).thenReturn(managementUri);
        String dataUri = "http://localhost:8888/data";
        when(mockConfig.getString(Configurator.REST_API_PATH_PROPERTY_KEY)).thenReturn(dataUri);
        
        
        DiscoveryService ds = new DiscoveryService(mockConfig, new EntityOutputFormat( new JsonFormat(), new URI("http://localhost:5555"), null ));
        Response response = ds.getDiscoveryDocument();
        
        String json = new String((byte[]) response.getEntity());
        
        assertNotNull(json);
        assertThat(json.length(), is(greaterThan(0)));
        assertThat(json, is(not("\"\"")));
        assertThat(json, is(not("null")));
        
        assertThat(json, containsString("\"management\" : \"" + managementUri + "\""));
        assertThat(json, containsString("\"data\" : \"" + dataUri + "\""));
    }
}