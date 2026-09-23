/**
 * Renders the swagger file chosen in the select with Swagger UI, and renders the next one on each change.
 */
document.addEventListener( 'DOMContentLoaded', () => {
    const select = document.getElementById( 'swagger-select' );
    if ( !select || !select.value ) {
        return;
    }
    const ui = SwaggerUIBundle( {
        url: select.value,
        dom_id: '#swagger-ui-container',
        defaultModelRendering: 'model',
        supportedSubmitMethods: [ 'get', 'post', 'put', 'delete', 'patch' ],
        oauth2RedirectUrl: select.dataset.oauth2RedirectUrl
    } );
    select.addEventListener( 'change', () => {
        ui.specActions.updateUrl( select.value );
        ui.specActions.download( select.value );
    } );
} );
