
#Plugin swaggerui

![](http://dev.lutece.paris.fr/plugins/plugin-swaggerui/images/swagger-logo.png)

##Introduction

This plugin displays all JSON or YAML swagger files present on a website and display them on a single page using the [Swagger](swagger.io) UI

##Configuration

Simply put the files by following this tree : webapp/plugins/{name-of-the-plugin}/api/swagger/v{version-number}/{name-of-the-file}[.json/.yaml]

Using Pluginwizard, it creates and put the file swagger.json in the right place

For API deployed on several servers, it's possible to use variables into JSON or YAML files **host** , **port** et **context** as follow :


```

"host": "${host}:${port}",
"basePath": "${context}/rest/lutecetools"

```


or in YAML


```

host: '${host}:${port}'
basePath: '${context}/rest/lutecetools'

```


##Usage

You just have to access the xpage swaggerui : /jsp/site/Portal.jsp?page=swaggerui

Note that the plugin swaggerui only displays the json files of enabled plugins


[Maven documentation and reports](http://dev.lutece.paris.fr/plugins/plugin-swaggerui/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*