import {Component} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {environment} from "../environments/environment";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  private supportedLanguages = ['en', 'pl'];

  constructor(private translateService: TranslateService) {
    translateService.addLangs(this.supportedLanguages);
    translateService.setDefaultLang('en');
    const browserLang = translateService.getBrowserLang();
    translateService.use(this.supportedLanguages.indexOf(browserLang) > -1 ? browserLang : 'en');
  }

  get isAuthenticated() {
    return sessionStorage.getItem(environment.api.loggedUserStorage);
  }
}
