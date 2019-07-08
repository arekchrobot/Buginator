import {Component} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {SessionService} from "./shared/service/session.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  private supportedLanguages = ['en', 'pl'];

  constructor(private translateService: TranslateService, private sessionService: SessionService) {
    translateService.addLangs(this.supportedLanguages);
    translateService.setDefaultLang('en');
    const browserLang = translateService.getBrowserLang();
    translateService.use(this.supportedLanguages.indexOf(browserLang) > -1 ? browserLang : 'en');
  }

  get isAuthenticated() {
    return this.sessionService.isAuthenticated();
  }
}
