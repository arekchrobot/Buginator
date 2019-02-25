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
import {TokenInterceptor} from "./auth/interceptors/token.interceptor";
import {AuthService} from "./auth/auth.service";

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    LoginComponent,
    PasswordResetComponent,
    RegisterComponent
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
      useClass: TokenInterceptor,
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
