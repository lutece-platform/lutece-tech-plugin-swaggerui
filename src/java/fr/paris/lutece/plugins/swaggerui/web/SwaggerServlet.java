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
package fr.paris.lutece.plugins.swaggerui.web;

import fr.paris.lutece.plugins.swaggerui.service.SwaggerFileService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SwaggerServlet serve swagger files with dynamic host and base path
 */
public class SwaggerServlet extends HttpServlet
{

    private static final long serialVersionUID = -5713203328367191908L;
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_YAML = "application/x-yaml";
    private static final String MARK_HOST = "host";
    private static final String MARK_PORT = "port";
    private static final String MARK_CONTEXT = "context";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             the servlet Exception
     * @throws IOException
     *             the io exception
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        String strPathInfo = request.getPathInfo();
        int nPos = strPathInfo.indexOf( "/plugins" );
        String strFile = strPathInfo.substring( nPos );
        String strFileUrl = AppPathService.getAbsolutePathFromRelativePath( strFile );
        String strFileContent = readFile( strFileUrl, StandardCharsets.UTF_8 );

        Map<String, String> model = new ConcurrentHashMap<String, String>( );
        model.put( MARK_HOST, request.getServerName( ) );
        model.put( MARK_PORT, String.valueOf( request.getServerPort( ) ) );
        model.put( MARK_CONTEXT, request.getContextPath( ) );

        HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( strFileContent, Locale.getDefault( ), model );
        String strNewContent = template.getHtml( );
        
        String strContentType = CONTENT_TYPE_JSON;
        if( strFile.endsWith( SwaggerFileService.EXT_YAML))
        {
            strContentType = CONTENT_TYPE_YAML;
        }
        response.setContentType( strContentType );

        OutputStream out = response.getOutputStream( );
        out.write( strNewContent.getBytes( StandardCharsets.UTF_8 ) );
        out.flush( );
        out.close( );

    }

    /**
     * Read a File and returns its content as a string.
     * @param strFilePath The file path
     * @param encoding The encoding
     * @return The file content
     * @throws IOException If an error occurs
     */
    private static String readFile( String strFilePath, Charset encoding ) throws IOException
    {
        byte [ ] encoded = Files.readAllBytes( Paths.get( strFilePath ) );
        return new String( encoded, encoding );
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             the servlet Exception
     * @throws IOException
     *             the io exception
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             the servlet Exception
     * @throws IOException
     *             the io exception
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return message
     */
    @Override
    public String getServletInfo( )
    {
        return "Servlet serving swagger files with dynamic host and basepath";
    }

}
