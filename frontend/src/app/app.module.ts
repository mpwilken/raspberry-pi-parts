import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {HeaderComponent} from './raspi/components/header/header.component';
import {FooterComponent} from './raspi/components/footer/footer.component';
import {BasicAuthHttpInterceptor} from './raspi/interceptors/basic-auth-http-interceptor';
import {ApiHttpRequestInterceptor} from './raspi/interceptors/api-http-request-interceptor';
import {ApiEndpointService} from './raspi/interceptors/api-endpoint.service';
import {MessageService} from './raspi/services/message.service';
import {PartService} from './raspi/services';
import {LoginComponent} from './raspi/components/security/login/login.component';
import {AuthInterceptor} from './raspi/components/security/auth-interceptor.service';

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        FooterComponent,
        LoginComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,
    ],
    bootstrap: [AppComponent],
    providers: [
        {provide: HTTP_INTERCEPTORS, useClass: BasicAuthHttpInterceptor, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: ApiHttpRequestInterceptor, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
        ApiEndpointService,
        ApiHttpRequestInterceptor,
        BasicAuthHttpInterceptor,
        MessageService,
        PartService,
    ]
})
export class AppModule {
}
