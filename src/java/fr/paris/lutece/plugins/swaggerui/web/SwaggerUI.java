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
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides a simple implementation of an XPage
 */
@Controller( xpageName = "swaggerui" , pageTitleI18nKey = "swaggerui.xpage.swaggerui.pageTitle" , pagePathI18nKey = "swaggerui.xpage.swaggerui.pagePathLabel" )
public class SwaggerUI extends MVCApplication
{
    private static final String TEMPLATE_SWAGGERUI = "/skin/plugins/swaggerui/swaggerui.html";
    private static final String TEMPLATE_SWAGGERIFRAME = "/skin/plugins/swaggerui/swaggeriframe.html";
    private static final String VIEW_SWAGGERUI = "swaggerui";
    private static final String VIEW_SWAGGER_IFRAME = "swaggeriframe";
    private static final String MARK_SWAGGER_FILES_LIST = "swagger_files_list";
    private static final String MARK_SWAGGER_FILES = "swagger_files";
    private static final String MARK_BASE_URL = "base_url";
    
    /**
     * Returns the content of the page swaggerui. 
     * @param request The HTTP request
     * @return The view
     */
    @View( value = VIEW_SWAGGERUI , defaultView = true )
    public XPage viewSwaggerUI( HttpServletRequest request )
    {
        Map<String, Object> model = getModel(  );
        boolean swaggerFiles = true;
        
        if( SwaggerFileService.getSwaggerFiles( request ).isEmpty( ) )
        {
            swaggerFiles = false;
        }
        
        model.put( MARK_SWAGGER_FILES, swaggerFiles );
        return getXPage( TEMPLATE_SWAGGERUI, request.getLocale(  ), model );
    }
    
    @View( value = VIEW_SWAGGER_IFRAME )
    public XPage viewSwaggerIFrame( HttpServletRequest request )
    {
        Map<String, Object> model = getModel(  );
        model.put( MARK_SWAGGER_FILES_LIST, SwaggerFileService.getSwaggerFiles( request ) );
        model.put( MARK_BASE_URL , AppPathService.getBaseUrl( request ));

        XPage iFrame = getXPage( TEMPLATE_SWAGGERIFRAME, request.getLocale(  ), model );
        iFrame.setStandalone(true);
        return iFrame;
    }
}
