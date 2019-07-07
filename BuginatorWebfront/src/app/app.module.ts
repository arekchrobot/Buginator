import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthComponent} from './auth/auth.component';
import {ReactiveFormsModule} from "@angular/forms";
import {LoginComponent} from './auth/login/login.component';
import {PasswordResetComponent} from './auth/password-reset/password-reset.component';
import {RegisterComponent} from './auth/register/register.component';
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {CookieService} from "ngx-cookie-service";
import {OAuthTokenInterceptor} from "./auth/interceptors/oauth-token.interceptor";
import {AuthService} from "./auth/auth.service";
import { DashboardComponent } from './dashboard/dashboard.component';
import { ApplicationListComponent } from './application/application-list/application-list.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    LoginComponent,
    PasswordResetComponent,
    RegisterComponent,
    DashboardComponent,
    ApplicationListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [
    CookieService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: OAuthTokenInterceptor,
      multi: true
    },
    {
      provide: APP_INITIALIZER,
      useFactory: (authService: AuthService) => function() {return authService.getLoggedUser()},
      deps: [AuthService],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
