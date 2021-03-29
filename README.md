# Raspberry Pi Parts

 This is a learning application for me that incorporates a variety of concepts.  It uses a very rudimentary
UI (I am not a designer) and a Spring Boot backend.  As I learn more and apply concepts I strive to keep
the tech stack up-to-date.

 * [Resources](#resources)

# Frontend

## Keep The System Current (KTSC)

The current [Angular CLI](https://github.com/angular/angular-cli) version of this project is 11.

Follow the [Angular Update Guide](https://update.angular.io/?v=9.0-11.0) when upgrading.  Note, if you
are upgrading to more than just the next major release, they recommend moving between major release not 
jumping to the latest.

See [below](#upgrading-angular) for example of previous ones.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

# Backend

## Running from command line

 Start the application using an in-memory database.
 
 ```bash
java -Dspring.profiles.active=h2,credentials -jar backend/target/parts-backend-?.?.?-SNAPSHOT.jar
 ```

 Open [browser](http://localhost:8080) and log in with whatever principal/credentials you configured the 
backend with (see [security](#security) below.).

 Or start them independently with the backend...

 ```bash
cd backend
../mvnw -Dspring.profiles.active=h2,credentials spring-boot:run
 ```

 Then start the frontend.  Be careful which url you open either localhost:4200 or localhost:8080, it will
behave differently.  Think about. ;)
 
 ```bash
cd frontend
ng serve
 ```

 You can then access the application at [here](http://localhost:4200)

## Security

Make sure to create an application-credentials.yml file with similar values to that below with values
generated similar to:

 ```java 
//generate a random HMAC
Key hs256Key = MacProvider.generateKey(SignatureAlgorithm.HS256);
Key hs384Key = MacProvider.generateKey(SignatureAlgorithm.HS384);

//Get the key data
byte hs256KeyData[]= hs256Key.getEncoded();
//Store data in a file...

//Build key
Key key = new SecretKeySpec(hs256KeyData, SignatureAlgorithm.HS256.getJcaName());
 ```

 ```yaml 
api:
  exposed:
    password: randompassword
    username: randomuser
app.security:
  jwt:
    expiration: 300
    HS256: "generatedfromabovecommands"
    HS384: "generatedfromabovecommands"
    HS512: "generatedfromabovecommands"
    issuer: thirdbase.us
    name: Authorization
    prefix: "Bearer "
  temporary:
    password: anotherrandompassword
    username: anotherrandomuser
 ```
## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).

# Load data from json

 ```bash
 echo '{ 
            "name": "OpenSprinkler Pi (OSPi)",
            "cost": 77.99,
            "quantity": 1,
            "description": "OpenSprinkler Pi (OSPi) is a sprinkler / irrigation extension board for Raspberry Pi (RPi). It allows RPi to directly access and control sprinkler valves. This is version 1.4 with OpenSprinkler injection molded enclosure. The built-in components include on-board 24V AC to 5V DC switching regulator, solenoid drivers, DS1307 RTC and battery, PCF8591T 8-bit A/D D/A converter (4 input and 1 output channels), fuse, rain sensor terminal, one general-purpose relay, and per-station transient voltage suppressor (bidirectional TVS).",
            "url": "http://rayshobby.net/cart/ospi",
            "images": [
                "ospi.jpg"
            ]
        }' | http POST :8080/api/parts
 ```

# Load testing

 See [Load Testing](./gatling/README.md)

# Logging

 You can view logging [here](http://localhost:8080/actuator/loggers) (enabled in application.yml)

## Change log levels at runtime

 Turn on timing for SystemService calls:
 
 ```bash
curl -i -X POST -H 'Content-Type: application/json' -d '{"configuredLevel": "TRACE"}' http://localhost:8080/actuator/loggers/org.springframework.aop.interceptor.PerformanceMonitorInterceptor
 ```

 Turn off
  
 ```bash
curl -i -X POST -H 'Content-Type: application/json' -d '{"configuredLevel": "INFO"}' http://localhost:8080/actuator/loggers/org.springframework.aop.interceptor.PerformanceMonitorInterceptor
 ```

# Troubleshooting

 ``` 
Chrome version must be between 71 and 75
 ```

 Running e2e tests, if this error occurs run 

 ```bash
pushd cd ./node_modules/protractor npm i webdriver-manager@latest && popd 
 ```

# Upgrading Angular

 ```bash
alias ng='./node_modules/@angular/cli/bin/ng'
ng --version

     _                      _                 ____ _     ___
    / \   _ __   __ _ _   _| | __ _ _ __     / ___| |   |_ _|
   / △ \ | '_ \ / _` | | | | |/ _` | '__|   | |   | |    | |
  / ___ \| | | | (_| | |_| | | (_| | |      | |___| |___ | |
 /_/   \_\_| |_|\__, |\__,_|_|\__,_|_|       \____|_____|___|
                |___/


Angular CLI: 9.0.2
Node: 12.20.1
OS: darwin arm64

Angular: 9.0.1
... animations, common, compiler, compiler-cli, core, forms
... language-service, localize, platform-browser
... platform-browser-dynamic, router
Ivy Workspace: Yes

Package                           Version
-----------------------------------------------------------
@angular-devkit/architect         0.900.2
@angular-devkit/build-angular     0.900.2
@angular-devkit/build-optimizer   0.900.2
@angular-devkit/build-webpack     0.900.2
@angular-devkit/core              9.0.2
@angular-devkit/schematics        9.0.2
@angular/cli                      9.0.2
@ngtools/webpack                  9.0.2
@schematics/angular               9.0.2
@schematics/update                0.900.2
rxjs                              6.5.4
typescript                        3.7.5
webpack                           4.41.2 
 ```

 ```bash
ng update @angular/core@10 @angular/cli@10 --force

The installed local Angular CLI version is older than the latest stable version.
Installing a temporary version to perform the update.
Installing packages for tooling via npm.
Installed packages for tooling via npm.
Using package manager: 'npm'
Collecting installed dependencies...
Found 43 dependencies.
Fetching dependency metadata from registry...
                  Package "@ng-bootstrap/ng-bootstrap" has an incompatible peer dependency to "@angular/common" (requires "^8.0.0" (extended), would install "10.2.4").
                  Package "@ng-bootstrap/ng-bootstrap" has an incompatible peer dependency to "@angular/core" (requires "^8.0.0" (extended), would install "10.2.4").
                  Package "@ng-bootstrap/ng-bootstrap" has an incompatible peer dependency to "@angular/forms" (requires "^8.0.0" (extended), would install "10.2.4").
                  Package "ngx-toastr" has an incompatible peer dependency to "@angular/platform-browser" (requires ">=6.0.0 <9.0.0" (extended), would install "10.2.4").
    Updating package.json with dependency @angular-devkit/build-angular @ "0.1002.1" (was "0.900.2")...
    Updating package.json with dependency @angular/cli @ "10.2.1" (was "9.0.2")...
    Updating package.json with dependency @angular/compiler-cli @ "10.2.4" (was "9.0.1")...
    Updating package.json with dependency @angular/language-service @ "10.2.4" (was "9.0.1")...
    Updating package.json with dependency typescript @ "4.0.5" (was "3.7.5")...
    Updating package.json with dependency @angular/animations @ "10.2.4" (was "9.0.1")...
    Updating package.json with dependency @angular/common @ "10.2.4" (was "9.0.1")...
    Updating package.json with dependency @angular/compiler @ "10.2.4" (was "9.0.1")...
    Updating package.json with dependency @angular/core @ "10.2.4" (was "9.0.1")...
    Updating package.json with dependency @angular/forms @ "10.2.4" (was "9.0.1")...
    Updating package.json with dependency @angular/localize @ "10.2.4" (was "9.0.1")...
    Updating package.json with dependency @angular/platform-browser @ "10.2.4" (was "9.0.1")...
    Updating package.json with dependency @angular/platform-browser-dynamic @ "10.2.4" (was "9.0.1")...
    Updating package.json with dependency @angular/router @ "10.2.4" (was "9.0.1")...
    Updating package.json with dependency zone.js @ "0.10.3" (was "0.10.2")...
  UPDATE package.json (2483 bytes)
✔ Packages installed successfully.
** Executing migrations of package '@angular/cli' **

❯ Update Browserslist configuration file name to '.browserslistrc' from deprecated 'browserslist'.
  RENAME src/browserslist => src/.browserslistrc
  Migration completed.

❯ Update tslint to version 6 and adjust rules to maintain existing behavior.
    tslint configuration does not extend "tslint:recommended" or it extends multiple configurations.
    Skipping rule changes as some rules might conflict.
  UPDATE package.json (2483 bytes)
  UPDATE tslint.json (2802 bytes)
  Migration completed.

❯ Remove deprecated 'es5BrowserSupport' browser builder option.
  The inclusion for ES5 polyfills will be determined from the browsers listed in the browserslist configuration.
  Migration completed.

❯ Replace deprecated and removed 'styleext' and 'spec' Angular schematic options with 'style' and 'skipTests', respectively.
  Migration completed.

❯ Remove deprecated options from 'angular.json' that are no longer present in v10.
  Migration completed.

❯ Add the tslint deprecation rule to tslint JSON configuration files.
  Migration completed.

❯ Update library projects to use tslib version 2 as a direct dependency.
  Read more about this here: https://v10.angular.io/guide/migration-update-libraries-tslib
  Migration completed.

❯ Update workspace dependencies to match a new v10 project.
  UPDATE package.json (2482 bytes)
✔ Packages installed successfully.
  Migration completed.

❯ Update 'module' and 'target' TypeScript compiler options.
  Read more about this here: https://v10.angular.io/guide/migration-update-module-and-target-compiler-options
    Unable to update 'e2e/tsconfig.json' target option from 'es5' to 'es2018': Could not read 'e2e/tsconfig.json'.
  UPDATE tsconfig.json (435 bytes)
  Migration completed.

❯ Removing "Solution Style" TypeScript configuration file support.
    Migration has already been executed.
  Migration completed.

** Executing migrations of package '@angular/core' **

❯ Missing @Injectable and incomplete provider definition migration.
  As of Angular 9, enforcement of @Injectable decorators for DI is a bit stricter and incomplete provider definitions behave differently.
  Read more about this here: https://v9.angular.io/guide/migration-injectable
  Migration completed.

❯ ModuleWithProviders migration.
  As of Angular 10, the ModuleWithProviders type requires a generic.
  This migration adds the generic where it is missing.
  Read more about this here: https://v10.angular.io/guide/migration-module-with-providers
  Migration completed.

❯ Undecorated classes with Angular features migration.
  In version 10, classes that use Angular features and do not have an Angular decorator are no longer supported.
  Read more about this here: https://v10.angular.io/guide/migration-undecorated-classes
  Migration completed.
 ```

Determine if everything is still functioning as expected before moving to the next major version.

 ```bash
ng update @angular/core @angular/cli --force

The installed local Angular CLI version is older than the latest stable version.
Installing a temporary version to perform the update.
Installing packages for tooling via npm.
Installed packages for tooling via npm.
Using package manager: 'npm'
Collecting installed dependencies...
Found 43 dependencies.
Fetching dependency metadata from registry...
                  Package "@ng-bootstrap/ng-bootstrap" has an incompatible peer dependency to "@angular/common" (requires "^8.0.0" (extended), would install "11.1.1").
                  Package "codelyzer" has an incompatible peer dependency to "@angular/compiler" (requires ">=2.3.1 <10.0.0 || >9.0.0-beta <10.0.0 || >9.1.0-beta <10.0.0 || >9.2.0-beta <10.0.0" (extended), would install "11.1.1").
                  Package "codelyzer" has an incompatible peer dependency to "@angular/core" (requires ">=2.3.1 <10.0.0 || >9.0.0-beta <10.0.0 || >9.1.0-beta <10.0.0 || >9.2.0-beta <10.0.0" (extended), would install "11.1.1").
                  Package "@ng-bootstrap/ng-bootstrap" has an incompatible peer dependency to "@angular/forms" (requires "^8.0.0" (extended), would install "11.1.1").
                  Package "ngx-toastr" has an incompatible peer dependency to "@angular/platform-browser" (requires ">=6.0.0 <9.0.0" (extended), would install "11.1.1").
    Updating package.json with dependency @angular-devkit/build-angular @ "0.1101.2" (was "0.1002.1")...
    Updating package.json with dependency @angular/cli @ "11.1.2" (was "10.2.1")...
    Updating package.json with dependency @angular/compiler-cli @ "11.1.1" (was "10.2.4")...
    Updating package.json with dependency @angular/language-service @ "11.1.1" (was "10.2.4")...
    Updating package.json with dependency karma @ "5.2.3" (was "5.0.9")...
    Updating package.json with dependency @angular/animations @ "11.1.1" (was "10.2.4")...
    Updating package.json with dependency @angular/common @ "11.1.1" (was "10.2.4")...
    Updating package.json with dependency @angular/compiler @ "11.1.1" (was "10.2.4")...
    Updating package.json with dependency @angular/core @ "11.1.1" (was "10.2.4")...
    Updating package.json with dependency @angular/forms @ "11.1.1" (was "10.2.4")...
    Updating package.json with dependency @angular/localize @ "11.1.1" (was "10.2.4")...
    Updating package.json with dependency @angular/platform-browser @ "11.1.1" (was "10.2.4")...
    Updating package.json with dependency @angular/platform-browser-dynamic @ "11.1.1" (was "10.2.4")...
    Updating package.json with dependency @angular/router @ "11.1.1" (was "10.2.4")...
  UPDATE package.json (2481 bytes)
✔ Packages installed successfully.
** Executing migrations of package '@angular/cli' **

❯ Replace deprecated library builder '@angular-devkit/build-ng-packagr'.
  Migration completed.

❯ Add 'declarationMap' compiler options for non production library builds.
  Migration completed.

❯ Remove deprecated options from 'angular.json' that are no longer present in v11.
  UPDATE angular.json (5121 bytes)
  Migration completed.

❯ Update workspace dependencies to match a new v11 project.
  UPDATE package.json (2482 bytes)
✔ Packages installed successfully.
  Migration completed.

** Executing migrations of package '@angular/core' **

❯ In Angular version 11, the type of `AbstractControl.parent` can be `null` to reflect the runtime value more accurately.
  This migration automatically adds non-null assertions to existing accesses of the `parent` property on types like `FormControl`, `FormArray` and `FormGroup`.
  Migration completed.

❯ ViewEncapsulation.Native has been removed as of Angular version 11.
  This migration replaces any usages with ViewEncapsulation.ShadowDom.
  Migration completed.

❯ NavigationExtras omissions migration.
  In version 11, some unsupported properties were omitted from the `extras` parameter of the `Router.navigateByUrl` and `Router.createUrlTree` methods.
  Migration completed.

❯ Updates the `initialNavigation` property for `RouterModule.forRoot`.
  Migration completed.

❯ NavigationExtras.preserveQueryParams has been removed as of Angular version 11.
   This migration replaces any usages with the appropriate assignment of the queryParamsHandling key.
  Migration completed.

❯ The default value for `relativeLinkResolution` is changing from 'legacy' to 'corrected'.
This migration updates `RouterModule` configurations that use the default value to
now specifically use 'legacy' to prevent breakages when updating.
  UPDATE src/app/app-routing.module.ts (715 bytes)
  Migration completed.

❯ `async` to `waitForAsync` migration.
  The `async` testing function has been renamed to `waitForAsync` to avoid confusion with the native `async` keyword.
  UPDATE src/app/app.component.spec.ts (986 bytes)
  UPDATE src/app/raspi/components/footer/footer.component.spec.ts (692 bytes)
  UPDATE src/app/raspi/components/header/header.component.spec.ts (3863 bytes)
  UPDATE src/app/raspi/components/part/part.component.spec.ts (1180 bytes)
  UPDATE src/app/raspi/components/part-detail/part-detail.component.spec.ts (1845 bytes)
  UPDATE src/app/raspi/components/part-list/part-list.component.spec.ts (6750 bytes)
  UPDATE src/app/raspi/services/part.service.spec.ts (3295 bytes)
  Migration completed.

❯ Removes `canActivate` from a `Route` config when `redirectTo` is also present.
  UPDATE src/app/raspi/raspi-routing.module.ts (731 bytes)
  UPDATE src/app/app-routing.module.ts (684 bytes)
  Migration completed.
 ```

 After the latest upgrade.

 ```bash
 ng --version

     _                      _                 ____ _     ___
    / \   _ __   __ _ _   _| | __ _ _ __     / ___| |   |_ _|
   / △ \ | '_ \ / _` | | | | |/ _` | '__|   | |   | |    | |
  / ___ \| | | | (_| | |_| | | (_| | |      | |___| |___ | |
 /_/   \_\_| |_|\__, |\__,_|_|\__,_|_|       \____|_____|___|
                |___/


Angular CLI: 11.1.2
Node: 12.20.1
OS: darwin arm64

Angular: 11.1.1
... animations, common, compiler, compiler-cli, core, forms
... language-service, localize, platform-browser
... platform-browser-dynamic, router
Ivy Workspace: Yes

Package                         Version
---------------------------------------------------------
@angular-devkit/architect       0.1101.2
@angular-devkit/build-angular   0.1101.2
@angular-devkit/core            11.1.2
@angular-devkit/schematics      11.1.2
@angular/cli                    11.1.2
@schematics/angular             11.1.2
@schematics/update              0.1101.2
rxjs                            6.5.4
typescript                      4.0.5
 ```

# Docker 

 Build image 

 ```bash
docker build -t parts .
 ```

 Tag image

 ```bash
docker tag parts localhost:5000/parts
 ```

 Push image

 ```bash
docker push localhost:5000/parts
 ```

 Run image

 ```bash
docker run -it -p 8080:8080 parts /bin/bash
 ```

# Resources

 * [Security of JSON Web Tokens (JWT)](https://cyberpolygon.com/materials/security-of-json-web-tokens-jwt/)
