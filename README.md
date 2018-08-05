<img src="doc/img/sited-logo.png" align="right" />

# Sited CMS

A developer friendly Java CMS based on Java 10 micro service modules with full API access to contents, users and files. 

- **Built for startup, provides well designed quick prototyping code base.**
- Supports template editing, perfect for building landing pages.
- More modules are coming...


[![Edit Template](doc/img/edit-template.png)](https://github.com/sited-io/sited-project/raw/master/doc/img/edit-template.png) | [![Edit Page](doc/img/edit-page.png)](https://github.com/sited-io/sited-project/raw/master/doc/img/edit-page.png) |
|:---:|:---:|
[Edit Template](doc/img/edit-template.png) | [Edit Page](doc/img/edit-page.png) |
| [![Index Page](doc/img/index.png)](https://github.com/sited-io/sited-project/raw/master/doc/img/index.png)  |  [![Post Page](doc/img/page.png)](https://github.com/sited-io/sited-project/raw/master/doc/img/page.png) |
| [Index Page](http://) | [Post Page](http://) |

## Features

|Platform| Windows/Linux/Mac OS|
|:---:|:---:|
|Database|MySQL and HSQL are tested. For other databases, need to manually install drivers|
|Hardware|AWS micro instance is tested (1 core, 1G mem)|
|Frontend Browser Compatibility|Build with jQuery and bootstrap, requires IE8+|
|Admin Browser Compatibility|Build with React, requires IE11+|
|Deployment|Standalone instance or deploy API/Web/Admin separately|

Sited CMS includes a light JAX-RS module framework and basic CMS modules. 

* JAX-RS module framework
  * Enhancements to Jersey
  * Supports Guice style DI/AOP 
  * Embedded HTTP server with undertow

- Content Management
  * Full API for Page/Page Category/Template/Template components/Variables/Tag
  * Edit templates by drag&drop template components
  * Support drafts
  * Use markdown as default editor
  * Supports dynamic fields
  * OG meta supports
  * Baidu, addthis social share supports
  * Baidu, GA statistics supports
  * Web pages
    * Index Page
    * Post category page
    * Post pages
    * Tag page list
   * Web components
     * Header
     * Footer 
     * Category page list with pagination support
     * Recently page list
     * Popular page list
     * Related page list
     * Next/Pre pages
     * Breadcrumb
     * Card for any content
     * Html for any content
     * Content table
     * Category Tree
     * Banner
     * Social share
 
- User management
   * Full API for User/Role/Permission.
   * Declarative permission validation.
   * Supports dynamic fields. 
   * Email pincode
   * Simple captcha code
   * Web pages
     * Login
     * Register
     * Forget password
     * Reset password
   
- File management
   * Create database record for upload file for file access permission(Not quite ready yet)
   * File browser
 

## Getting Started

These instructions will get you a copy of the Sited CMS up and running on your local machine.<br>

### Prerequisites

1. Download and install [Open JDK 10](http://jdk.java.net/10/) or [Oracle JDK 10](http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html)
2. If you want to use MySQL as database. (***Optional, Sited default embeds HSQL***)
   1. Download and install [MySQL](https://dev.mysql.com/downloads/mysql/). 
   2. Create a database. <br>
   ```CREATE DATABASE main CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;```
   3. Create a database user with schema update permission. <br>
   ```   CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';   GRANT ALL PRIVILEGES ON * . * TO 'user'@'localhost';   FLUSH PRIVILEGES;   ```

### Installing

1. Download the package file [sited-v0.9.0-beta.zip](https://github.com/sited-io/sited-project/releases/download/v0.9.0/sited-v0.9.0-beta.zip) (***For all platform***)
2. Unzip the package
3. Run `./bin/sited`
4. Use a browser(IE11+) to open ```http://localhost:8080```
5. Fill in the require information to setup sited. 
   1. Choose language. 
   2. Input app name, the name will be displayed in page title. 
   3. Select database. 
      1. If you want to use MySQL, input the info of database and user you created.
   4. Input SMTP settings. 
      > Optional, if you skip the SMTP settings, user register will be disabled.
   5. Click install button. Sited will restart.
6. Open ```http://localhost:8080/admin/```


## Run Source Code

1. Clone the repo
2. Import as a Gradle project to Intellij IDEA or Eclipse.
3. Run sited-main/src/java/Main.java 

### Known issues

* There will be errors in module-info.java for duplicate module java.xml.bind
* There will be errors in DAO related source codes for missing @Transactional. 

It is because of Java 10 JEE split package issues. A temp fix for Intellij IDEA is: 
1. Open settings. Build, Execution, Deployment>Compiler>Java Compiler. 
2. Input the following parameters to Additional command line parameters. And replace the path of javax.transaction-api-1.3.jar. 
   ```
   --add-modules=java.xml.bind --patch-module java.transaction=~\.gradle\caches\modules-2\files-2.1\javax.transaction\javax.transaction-api\1.3\e006adf5cf3cca2181d16bd640ecb80148ec0fce\javax.transaction-api-1.3.jar
   ```
3. The error messages are still there, but it compiles. 

## Code Examples

To start an `App`: 

```
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Path dir = Paths.get(System.getProperty("user.home")).resolve(".sited");
        App app = new UndertowApp(dir);
        ServiceLoader.load(AbstractModule.class).forEach(app::install);
        app.start();
    }
}
```

To create a `Module`:
```

public class TodoServiceModuleImpl extends TodoServiceModule {
    @Override
    protected void configure() {
        //import DatabaseModule to register entity and create repository
        module(DatabaseModule.class)
            .entity(Task.class);
        
        bind(TaskService.class);
        
        //register service implementation
        api().service(TaskWebService.class, TaskWebServiceImpl.class);
    }
}
```


## Release Notes

* 0.9.1 
  * Clear modules, remove page search/rss and edm features.
  * Add GA/addthis modules

* 0.9.0
  * User management, supports login/register/forget password/pincode/captcha code.
  * Page management, supports category/page/template/variable/components.
  * File management, supports upload files, download files, scale images, react file browser. 
  * Basic email template support

## Authors

* Chi
  > If you need support for customization or commerce license, please feel free to contact me ``chiron.chi#gmail.com``

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.


## License

This project is licensed under the AGPL License - see the [LICENSE.md](LICENSE.md) file for details


