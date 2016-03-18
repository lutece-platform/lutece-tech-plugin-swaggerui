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
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppPathService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * This service searchs swagger.json files
 */
public class SwaggerFileService
{
    private static final String SWAGGER_DIRECTORY_NAME = "swagger";
    private static final String SWAGGER_DIRECTORY_PATH = "/plugins";
    
    private static final String SWAGGER_REPLACE_PATH_DIRECTORY = "plugins/";
    private static final String SWAGGER_REPLACE_PATH_REST = "rest/";
    private static final String SWAGGER_REPLACE_PATH_SWAGGER = "swagger/";
    
    /**
     * Returns the list of swagger files 
     * @param request
     * @return The list of swagger files
     */
    public static List<SwaggerFile> getSwaggerFiles( HttpServletRequest request )
    {
        List<SwaggerFile> listSwaggerFiles = new ArrayList<>();
        List<File> listSwaggerDirectories = new ArrayList<>( );
        String[] filesExtension = {"json"};
        File folderWebApp = new File( AppPathService.getWebAppPath( ) + SWAGGER_DIRECTORY_PATH );
        findDirectory( listSwaggerDirectories, folderWebApp );
        
        for ( File swaggerDirectory : listSwaggerDirectories )
        {
            Collection<File> filesSwagger = FileUtils.listFiles( swaggerDirectory, filesExtension, true);
            for (File fileSwagger : filesSwagger)
            {
                String strPluginName = swaggerDirectory.getParentFile( ).getParentFile().getName( );
                if( PluginService.isPluginEnable( strPluginName ) )
                {
                    SwaggerFile swaggerFile = new SwaggerFile();
                    swaggerFile.setPluginName( strPluginName );
                    swaggerFile.setVersion( fileSwagger.getParentFile( ).getName( ) );

                    String relativePath = new File( AppPathService.getWebAppPath( ) ).toURI( ).relativize( fileSwagger.toURI( ) ).getPath( );
                    swaggerFile.setPath( AppPathService.getBaseUrl( request ) + replacePath( relativePath ) );

                    listSwaggerFiles.add( swaggerFile );
                }
            }
        }
        return listSwaggerFiles;
    }
    
    private static void findDirectory( List<File> listSwaggerDirectories, File parentDirectory )
    {
        File[] listFiles = parentDirectory.listFiles( );
        if( listFiles != null )
        {
            for ( File file : listFiles )
            {
                if ( file.isFile( ) )
                {
                    continue;
                }
                if (file.getName( ).equals( SWAGGER_DIRECTORY_NAME ) )
                {
                    listSwaggerDirectories.add( file );
                }
                if(file.isDirectory( ) )
                {
                   findDirectory( listSwaggerDirectories, file );
                }
            }
        }
    }
    
    private static String replacePath( String strPath )
    {
        strPath = strPath.replace( SWAGGER_REPLACE_PATH_DIRECTORY , SWAGGER_REPLACE_PATH_REST );
        strPath = strPath.replace( SWAGGER_REPLACE_PATH_SWAGGER , StringUtils.EMPTY );
        return strPath;
    }
}
