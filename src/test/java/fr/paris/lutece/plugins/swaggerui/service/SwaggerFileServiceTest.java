/*
 * Copyright (c) 2002-2016, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.swaggerui.service;

import fr.paris.lutece.plugins.swaggerui.business.SwaggerFile;
import fr.paris.lutece.test.LuteceTestCase;

import org.junit.jupiter.api.Test;

/**
 * Tests the lookup of the swagger files
 */
public class SwaggerFileServiceTest extends LuteceTestCase
{
    private static final String BASE_URL = "http://localhost/lutece/";

    /**
     * Only a swagger file of an enabled plugin is served, never another file of the webapp
     */
    @Test
    public void testGetSwaggerFileRefusesOtherFiles( )
    {
        assertNull( SwaggerFileService.getSwaggerFile( "WEB-INF/web.xml" ) );
        assertNull( SwaggerFileService.getSwaggerFile( "../pom.xml" ) );
        assertNull( SwaggerFileService.getSwaggerFile( "plugins/unknown/api/swagger/v1/swagger.json" ) );
    }

    /**
     * Every listed swagger file is served by the plugin servlet and resolves back to a file
     */
    @Test
    public void testGetSwaggerFiles( )
    {
        String strServletUrl = BASE_URL + "servlet/plugins/swaggerui/";

        for ( SwaggerFile swaggerFile : SwaggerFileService.getSwaggerFiles( BASE_URL ) )
        {
            assertTrue( swaggerFile.getPath( ).startsWith( strServletUrl ) );
            assertNotNull( SwaggerFileService.getSwaggerFile( swaggerFile.getPath( ).substring( strServletUrl.length( ) ) ) );
        }
    }
}
