# Sited CMS

A developer friendly Java CMS based on JAX-RS, Guice style DI, Bean Validation, JPA and React. 

- Provides a light framework for JAX-RS modules.
- A new Java template engine to support page widgets
- Basic CMS features
- More components are coming


//TODO

## Demo 

Demo Blog//TODO

## Installing

1. Install JDK 10. 
2. Download dist zip file (For all platform)
3. Unzip the package
4. Run `./bin/sited`


## Configuration

Here is a default configuration file.

```
app:
  name: test
  language: en-US
  env: PROD

database:
  url: jdbc:mysql://localhost:3306/main?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
  username: root
  createTableEnabled: false

smtp:
  host: smtp-mail.outlook.com
  port: 587
  username: sited.io@outlook.com
  password: Sited123!A
  starttls: true

web:
  cacheEnabled: false
  roots:
    - ./sited-admin/src/main/dist/web
    - ./sited-email/email-admin/src/main/dist/web
    - ./sited-user/user-admin/src/main/dist/web
    - ./sited-user/user-web/src/main/web
    - ./sited-page/page-admin/src/main/dist/web
    - ./sited-page/page-web/src/main/web
    - ./sited-comment/comment-admin/src/main/dist/web
    - ./sited-comment/comment-web/src/main/web
    - ./sited-file/file-admin/src/main/dist/web
    - ./sited-page/page-tracking-admin/src/main/dist/web
    - ./sited-page/page-tracking-web/src/main/web
    - ./sited-page/page-search-admin/src/main/dist/web
    - ./sited-page/page-search-web/src/main/web
    - ./sited-captcha/captcha-web-simple/src/main/web

user:
  defaultAdminUser:
    username: admin
    password: admin
```


## Run from source

1. Clone the repo
2. Run Main from sited-main module


## Code Examples

To start Sited `App`: 
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

To create a Sited ``Module`` with **Guice** like DSL: 
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

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Release Notes


## License

This project is licensed under the AGPL License. 
If you need support or customization, please feel free to contact me, chiron.chi#gmail.com. 
