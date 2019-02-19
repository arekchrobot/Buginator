import {Component} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'buginator-webfront';

  private supportedLanguages = ['en', 'pl'];

  constructor(private translateService: TranslateService) {
    translateService.addLangs(this.supportedLanguages);
    translateService.setDefaultLang('en');
    const browserLang = translateService.getBrowserLang();
    translateService.use(this.supportedLanguages.indexOf(browserLang) > -1 ? browserLang : 'en');
  }
}
