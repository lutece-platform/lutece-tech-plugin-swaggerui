function loadSwaggerFile(url) {
    var swagger = new SwaggerUi({
        url:url,
        dom_id:"swagger-ui-container"
    });
    swagger.load();
}