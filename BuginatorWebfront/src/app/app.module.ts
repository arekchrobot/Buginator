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
import {DashboardComponent} from './dashboard/dashboard.component';
import {ApplicationListComponent} from './application/application-list/application-list.component';
import {NgxPaginationModule} from "ngx-pagination";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastrModule} from "ngx-toastr";
import { ApplicationCreateComponent } from './application/application-create/application-create.component';
import {SessionService} from "./shared/service/session.service";
import {Router} from "@angular/router";
import {UnauthorizedInterceptor} from "./auth/interceptors/unauthorized.interceptor";
import { ApplicationDetailsComponent } from './application/application-details/application-details.component';
import { ErrorDetailsComponent } from './error/error-details/error-details.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    LoginComponent,
    PasswordResetComponent,
    RegisterComponent,
    DashboardComponent,
    ApplicationListComponent,
    ApplicationCreateComponent,
    ApplicationDetailsComponent,
    ErrorDetailsComponent
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
    }),
    NgxPaginationModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      maxOpened: 3,
      preventDuplicates: true,
      countDuplicates: true,
      positionClass: 'toast-top-right'
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
    },
    {
      provide: HTTP_INTERCEPTORS,
      useFactory: UnauthorizedInterceptorFactory,
      deps:[Router, SessionService],
      multi: true
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

export function UnauthorizedInterceptorFactory(router: Router, sessionService: SessionService) {
  return new UnauthorizedInterceptor(router, sessionService);
}
