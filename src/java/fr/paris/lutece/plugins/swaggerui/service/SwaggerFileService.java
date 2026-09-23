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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.io.FileUtils;

/**
 * This service searchs swagger.json files
 */
public final class SwaggerFileService
{
    public static final String EXT_JSON = "json";
    public static final String EXT_YAML = "yaml";

    private static final String SWAGGER_DIRECTORY_NAME = "swagger";
    private static final String SWAGGER_DIRECTORY_PATH = "/plugins";
    private static final String MODULES_DIRECTORY_NAME = "modules";
    private static final String MODULE_NAME_SEPARATOR = "-";
    private static final String SERVLET_PATH = "servlet/plugins/swaggerui/";

    /**
     * private constructor
     */
    private SwaggerFileService( )
    {
    }

    /**
     * Returns the list of swagger files of the enabled plugins, sorted by path
     * 
     * @param strBaseUrl
     *            The base url of the webapp
     * @return The list of swagger files
     */
    public static List<SwaggerFile> getSwaggerFiles( String strBaseUrl )
    {
        List<SwaggerFile> listSwaggerFiles = new ArrayList<>( );
        for ( Map.Entry<File, String> descriptor : findDescriptors( ).entrySet( ) )
        {
            SwaggerFile swaggerFile = new SwaggerFile( );
            swaggerFile.setPluginName( descriptor.getValue( ) );
            swaggerFile.setVersion( descriptor.getKey( ).getParentFile( ).getName( ) );
            swaggerFile.setPath( strBaseUrl + SERVLET_PATH + getRelativePath( descriptor.getKey( ) ) );
            listSwaggerFiles.add( swaggerFile );
        }
        return listSwaggerFiles;
    }

    /**
     * Returns the swagger file of an enabled plugin matching a path relative to the webapp
     * 
     * @param strRelativePath
     *            The path relative to the webapp, as listed by getSwaggerFiles after the servlet path
     * @return The file, or null when no swagger file of an enabled plugin has this path
     */
    public static File getSwaggerFile( String strRelativePath )
    {
        return findDescriptors( ).keySet( ).stream( ).filter( file -> getRelativePath( file ).equals( strRelativePath ) ).findFirst( ).orElse( null );
    }

    /**
     * Finds the swagger files of the enabled plugins
     * 
     * @return The swagger files sorted by path, each with the name of the plugin that ships it
     */
    private static Map<File, String> findDescriptors( )
    {
        Map<File, String> mapDescriptors = new TreeMap<>( );
        List<File> listSwaggerDirectories = new ArrayList<>( );
        String [ ] filesExtension = {
            EXT_JSON , EXT_YAML
        };
        findDirectory( listSwaggerDirectories, new File( AppPathService.getWebAppPath( ) + SWAGGER_DIRECTORY_PATH ) );

        for ( File swaggerDirectory : listSwaggerDirectories )
        {
            String strPluginName = getPluginName( swaggerDirectory );
            if ( PluginService.isPluginEnable( strPluginName ) )
            {
                for ( File fileSwagger : FileUtils.listFiles( swaggerDirectory, filesExtension, true ) )
                {
                    mapDescriptors.put( fileSwagger, strPluginName );
                }
            }
        }
        return mapDescriptors;
    }

    /**
     * Returns the name of the plugin that ships a swagger directory: plugins/&lt;plugin&gt;/api/swagger, or
     * plugins/&lt;plugin&gt;/modules/&lt;module&gt;/api/swagger for the module named &lt;plugin&gt;-&lt;module&gt;
     * 
     * @param swaggerDirectory
     *            The swagger directory
     * @return The plugin name
     */
    private static String getPluginName( File swaggerDirectory )
    {
        File pluginDirectory = swaggerDirectory.getParentFile( ).getParentFile( );
        File modulesDirectory = pluginDirectory.getParentFile( );
        if ( MODULES_DIRECTORY_NAME.equals( modulesDirectory.getName( ) ) )
        {
            return modulesDirectory.getParentFile( ).getName( ) + MODULE_NAME_SEPARATOR + pluginDirectory.getName( );
        }
        return pluginDirectory.getName( );
    }

    /**
     * Returns the path of a file relative to the webapp
     * 
     * @param file
     *            The file
     * @return The relative path
     */
    private static String getRelativePath( File file )
    {
        return new File( AppPathService.getWebAppPath( ) ).toURI( ).relativize( file.toURI( ) ).getPath( );
    }

    /**
     * Find directories that contains swagger files
     * @param listSwaggerDirectories The directory list
     * @param parentDirectory The parent directory
     */
    private static void findDirectory( List<File> listSwaggerDirectories, File parentDirectory )
    {
        File [ ] listFiles = parentDirectory.listFiles( );
        if ( listFiles != null )
        {
            for ( File file : listFiles )
            {
                if ( file.isFile( ) )
                {
                    continue;
                }
                if ( file.getName( ).equals( SWAGGER_DIRECTORY_NAME ) )
                {
                    listSwaggerDirectories.add( file );
                }
                if ( file.isDirectory( ) )
                {
                    findDirectory( listSwaggerDirectories, file );
                }
            }
        }
    }

}
